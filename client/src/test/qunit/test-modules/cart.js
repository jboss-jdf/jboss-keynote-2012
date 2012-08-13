require([
    "namespace",
    "modules/cart"
],
function( ns, Cart ) {
    // Cart Model
    module( "Cart Module - Cart Model" );
    QUnit.test("Initialize the cart", function () {
        expect( 1 );
        var cart = new Cart.CartModel();
        ok( true, "Model initialized" );
    });

    // Cart page View
    module( "Cart Module - CartPage View", {
        setup: function() {
            $( "body" ).append( "<div id='cart-div'></div>" );
            this.cartView = new Cart.Views.CartPage();
        },
        teardown: function() {
            $( "#cart-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.cartView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.cartView.render( function() {
            $( "#cart-div" ).append( that.cartView.el );
            ok( $( ".catalog-item" ).length, "Empty cart view" );
            start();
        });
    });
});
