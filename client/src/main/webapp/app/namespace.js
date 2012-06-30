define([
    // Libs
    "jquery",
    "jquerym",
    "use!lodash",
    "use!backbone"
],

// Namespace the application to provide a mechanism for having application wide
// code without having to pollute the global namespace
function( $, $m, _, Backbone ) {
    // Serializes a form to a JavaScript Object
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each( a, function() {
            if ( o[ this.name ] ) {
                if ( !o[ this.name ].push ) {
                    o[ this.name ] = [ o[ this.name ] ];
                }
                o[ this.name ].push( this.value || '' );
            } else {
                o[ this.name ] = this.value || '';
            }
        });
        return o;
    };

    return {
        // Create a custom object with a nested Views object
        module: function( additionalProps ) {
            return _.extend( { Views: {} }, additionalProps );
        },

        // Keep active application instances namespaced under an app object.
        app: _.extend( {}, Backbone.Events ),

        // Used in place of jQuery Mobile's page transitioning mechanism
        transitioner: function( view, pageElement ) {
            $m.showPageLoadingMsg();
            pageElement.fadeOut( function() {
                view.render( function( el ) {
                    pageElement.html( el ).trigger( "pagecreate" ).fadeIn();
                    if ( localStorage.getItem( "cart" ) ) {
                        $( ".cart-total" ).html( $.parseJSON( localStorage.getItem( "cart" ) ).cartCount );
                    }
                    $m.hidePageLoadingMsg();
                });
            });
        },

        // Show a dialog message for status updates, errors, etc.
        showMessageDialog: function( closeSelector, afterURL, messageTemplate, messageText, closeTimer, router ) {
            var message = $( messageTemplate );
            message.find( ".message-body" ).text( messageText );
            message.appendTo( $( "body" ) ).dialog();
            $( closeSelector ).fadeOut( function() {
                message.fadeIn();
            });
            setTimeout( function(that) {
                message.fadeOut( function() {
                    $( this ).remove();
                    if ( !afterURL ) {
                        $( closeSelector ).fadeIn();
                    } else {
                        router.navigate( afterURL, { trigger: true } );
                    }
                });
            }, closeTimer);
        },

        // Clear local storage and forward to specified location
        clearStorage: function() {
            localStorage.removeItem( "user" );
            localStorage.removeItem( "cart" );
            localStorage.removeItem( "catalog" );
            localStorage.removeItem( "catalogTimestamp" );
        },

        // Store a var to hold the URL for easy swapping between local and hosted
        serviceURL: "/jbossworld"
    };
});
