define([
    "namespace",

    // Libs
    "jquery",
    "jquerym",
    "use!backbone",

    // Modules

    // Plugins

    //Templates
    "text!templates/orders.html",
    "text!templates/message.html"
],

function( namespace, $, $m, Backbone, orderListTemplate, messageTemplate ) {

    // Create a new order module
    var Orders = namespace.module();

    // Track our loop timer so we can cancel it when the user leaves the page
    var orderTimer;

    // Model representing an order
    Orders.Order = Backbone.Model.extend({});

    // Collection of order models representing all open orders
    Orders.OpenOrders = Backbone.Collection.extend({
        model: Orders.Order
    });

    // View to display the list of orders in the collection
    Orders.Views.OpenOrderView = Backbone.View.extend({
        orders: new Orders.OpenOrders,
        curLength: null,

        events: {
            "click #home-button": "clearOrderTimer",
            "click .next-order-button": "assignNextOrder",
            "click .assign-button": "assignOrder"
        },

        initialize: function( role ) {
            var view = this;

            // Store the role from the URL on the view instance
            this.urlRole = role;
            this.orders.url = namespace.serviceURL + "/order/open?userId=" + $.parseJSON( localStorage.getItem( "user" ) ).id;
        },

        render: function( done ) {
            var view = this,
                expandedAr = [],
                eventBindings = this.events;

            // Use an IIFE to execute the loop function immediately
            (function loop() {
                // Re-open any order details that the user had open when the list refreshed
                $( ".ui-collapsible" ).not( ".ui-collapsible-collapsed" ).each( function() {
                    expandedAr.push( $( this ).data( "order-id" ).toString() );
                });
                // Get all unassigned orders that this user can take control over
                view.orders.fetch({
                    success: function() {
                        // Only rebuild the UI if new orders have arrived
                        if ( view.curLength === null || view.orders.length != view.curLength ) {
                            view.curLength = view.orders.length;
                            view.el.innerHTML = _.template( orderListTemplate, { orders: view.orders.models, expanded: expandedAr, role: view.urlRole } );
                            
                            // If a done function is passed, call it with the element
                            if ( _.isFunction( done ) ) {
                                done( view.el );
                                view.delegateEvents( eventBindings );
                            }
                        }
                        // Long poll to update the order list every 5 seconds
                        orderTimer = setTimeout( function() {
                            expandedAr = [];
                            loop();
                        }, 5000);
                    },
                    error: function( collection, response ) {
                        var responseText = $.parseJSON( response.responseText );
                        if ( responseText ) {
                            namespace.showMessageDialog( "#main", "#" + $.parseJSON( localStorage.getItem( "user" ) ).role.toLowerCase() + "/role", messageTemplate, responseText.message, 2000, namespace.app.router );
                        } else {
                            // Server is most likely down. Set longer timeout to see if it will come back, most likely triggering a logout
                            namespace.showMessageDialog( "#main", false, messageTemplate, "Server Unavailable", 2000, namespace.app.router );
                            orderTimer = setTimeout( function() {
                                expandedAr = [];
                                loop();
                            }, 20000);
                        }
                    }
                });
            })();
        },

        assignNextOrder: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var user = $.parseJSON( localStorage.getItem( "user" ) );

            // Stop the order list loop
            clearTimeout( orderTimer );

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/order/nextOrder?userId=" + user.id,
                contentType: "application/x-www-form-urlencoded",
                type: "PUT",
                success: function() {
                    namespace.app.router.navigate( "#" + user.role.toLowerCase() + "/role", { trigger: true } );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "USER_ALREADY_ASSIGNEE":
                            namespace.showMessageDialog( "#main", "#" + user.role.toLowerCase() + "/role", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        case "INVALID_ROLE":
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

        assignOrder: function( event ) {
            // Don't add to browser history
            event.preventDefault();

            var user = $.parseJSON( localStorage.getItem( "user" ) ),
                orderID = $( event.target ).closest( ".assign-button" ).data( "order-id" );

            $m.showPageLoadingMsg();
            $.ajax({
                url: namespace.serviceURL + "/order/" + orderID + "/assign?userId=" + user.id,
                contentType: "application/x-www-form-urlencoded",
                type: "PUT",
                success: function() {
                    // Stop the order list loop
                    clearTimeout( orderTimer );
                    namespace.app.router.navigate( "#" + user.role.toLowerCase() + "/role", { trigger: true } );
                },
                error: function( jqXHR, textStatus, errorThrown ) {
                    var response = $.parseJSON( jqXHR.responseText );
                    switch ( response.type ) {
                        case "INVALID_ORDER":
                        case "ORDER_ALREADY_ASSIGNED":
                            namespace.showMessageDialog( "#main", false, messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "USER_ALREADY_ASSIGNEE":
                            // Stop the order list loop
                            clearTimeout( orderTimer );
                            namespace.showMessageDialog( "#main", "#" + user.role.toLowerCase() + "/role", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                        case "INVALID_USER":
                        case "INVALID_ROLE":
                        default:
                            // Stop the order list loop
                            clearTimeout( orderTimer );
                            namespace.showMessageDialog( "#main", "#logout", messageTemplate, response.message, 2000, namespace.app.router );
                            break;
                    }
                },
                done: function() {
                    $m.hidePageLoadingMsg();
                }
            });
        },

        clearOrderTimer: function() {
            clearTimeout( orderTimer );
        }
    });

    // Required, return the module for AMD compliance
    return Orders;

});
