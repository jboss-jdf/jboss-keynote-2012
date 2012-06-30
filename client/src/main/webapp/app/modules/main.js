define([
    "namespace",

    // Libs
    "jquery",
    "jquerym",
    "use!backbone",

    // Modules

    // Plugins

    //Templates
    "text!templates/main.html",
    "text!templates/role.html",
    "text!templates/message.html"
],

function( namespace, $, $m, Backbone, mainTemplate, roleTemplate, messageTemplate ) {

    // Create a new module
    var Main = namespace.module();

    // Default user model
    Main.User = Backbone.Model.extend({
        urlRoot: namespace.serviceURL + "/user",

        sync: function( method, model, options ) {
            var options = options || {};
            options.Accept = "application/json";
            return Backbone.sync( method, model, options );
        },

        validate: function( attributes ) {
            if ( !this.isNew() ) {
                return '{"message": "You may not change roles", "type": "INVALID_REGISTRATION"}';
            } else if ( attributes.name == "" ) {
                return '{"message": "Please enter your name", "type": "INVALID_REGISTRATION"}';
            }
        }
    });

    // Default view with registration form
    Main.Views.Registration = Backbone.View.extend({
        model: new Main.User,

        events: {
            "submit #profileForm": "register"
        },

        initialize: function( role ) {
            // Store the role from the URL on the view instance
            this.urlRole = role;

            // If the user is a buyer and don't have a cart object in local storage, create it
            if ( !localStorage.getItem( "cart" ) && role == "BUYER" ) {
                localStorage.setItem( "cart", '{"cartCount":0}' );
            }
        },

        render: function( done ) {
            var view = this,
                model = view.model.toJSON();
            model.role = view.urlRole;
            view.el.innerHTML = _.template( mainTemplate, model );

            // If a done function is passed, pass it the updated element for processing, display, etc.
            if ( _.isFunction( done ) ) {
                done( view.el );
            }

            // Update the stored user info when the model is updated
            view.model.on( "change", function( model ) {
                localStorage.setItem( "user", JSON.stringify( model ) );
            });
        },

        register: function( event ) {
            // Prevent the form from actually transitioning to its action
            event.preventDefault();

            var data = $( event.target ).serializeObject(),
                view = this;
            // jQM Serialize Workaround
            if ( data.register ) delete data.register;

            // Default new user to URL based role
            data.role = view.urlRole;

            // Create user
            view.model.save( data, {
                wait: true,
                success: function( model, response ) {
                    namespace.app.router.navigate( "#" + view.urlRole.toLowerCase() + "/role", { trigger: true } );
                },
                error: function( model, response ) {
                    var message = response.hasOwnProperty( "responseText" ) ?
                        $.parseJSON( response.responseText ).message : 
                        $.parseJSON( response ).message;
                    namespace.showMessageDialog( "#main", false, messageTemplate, message, 2000, namespace.app.router );
                }
            });
        }
    });

    // Registered user view with order summary
    Main.Views.Role = Backbone.View.extend({
        model: new Main.User,

        events: {
            "click #approveButton": "approveOrder",
            "click #rejectButton": "rejectOrder"
        },

        render: function( done ) {
            var view = this,
                localUser = $.parseJSON( localStorage.getItem( "user" ) );
            localUser.order = null;
            this.done = done

            $.ajax({
                url: namespace.serviceURL + "/user/" + localUser.id,
                success: function( data, textStatus, jqXHR ) {
                    localUser.totals = localUser.role != "BUYER" ? data.approverTotal : data.buyerTotal;

                    if ( localUser.role != "BUYER" ) {
                        $.ajax({
                            url: namespace.serviceURL + "/user/" + localUser.id + "/assignedOrder",
                            statusCode: {
                                200: function( data, textStatus, jqXHR ) {
                                    // Display the assigned order
                                    localUser.order = data;
                                    view.renderRole( localUser, done );
                                },
                                204: function() {
                                    //No assigned order
                                    localUser.order = {};
                                    view.renderRole( localUser, done );
                                }
                            }
                        });
                    } else {
                        view.renderRole( localUser, done );
                    }
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    namespace.clearStorage();
                    namespace.showMessageDialog( "#main", "#" + localUser.role.toLowerCase(), messageTemplate, $.parseJSON( jqXHR.responseText ).message, 2000, namespace.app.router );
                }
            });
        },

        renderRole: function( localUser, done ) {
            var view = this;

            view.el.innerHTML = _.template( roleTemplate, localUser );

            // If a done function is passed, pass it the updated element for processing, display, etc.
            if ( _.isFunction( done ) ) {
                done( view.el );
                view.delegateEvents( view.events );
            }
        },

        approveOrder: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var view = this;

            var user = $.parseJSON( localStorage.getItem( "user" ) ),
                orderID = $( "#approveButton" ).data( "order-id" );

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/order/" + orderID + "/approve?userId=" + user.id,
                contentType: "application/x-www-form-urlencoded",
                type: "PUT",
                success: function() {
                    view.render( view.done );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "INVALID_ORDER":
                        case "UNASSIGNED_ORDER":
                        case "ORDER_ASSIGNED_TO_OTHER":
                            namespace.showMessageDialog( "#main", "#" + user.role.toLowerCase() + "/orders", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        default:
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                    }
                    
                },
                done: function() {
                    $m.hidePageLoadingMsg();
                }
            });
        },

        rejectOrder: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var view = this;

            var user = $.parseJSON( localStorage.getItem( "user" ) ),
                orderID = $( "#rejectButton" ).data( "order-id" );

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/order/" + orderID + "/reject?userId=" + user.id + "&message=" + encodeURI( $( "#rejectMessage" ).val() ),
                contentType: "application/x-www-form-urlencoded",
                type: "PUT",
                success: function() {
                    view.render( view.done );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "INVALID_ORDER":
                        case "UNASSIGNED_ORDER":
                        case "ORDER_ASSIGNED_TO_OTHER":
                            namespace.showMessageDialog( "#main", "#" + user.role.toLowerCase() + "/orders", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        default:
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                    }
                    
                },
                done: function() {
                    $m.hidePageLoadingMsg();
                }
            });
        }
    });

    // Required, return the module for AMD compliance
    return Main;

});