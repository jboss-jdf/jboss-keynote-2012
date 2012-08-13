require([
    "namespace",
    "modules/orders"
],
function( ns, Orders ) {
    // Order Collection
    module( "Order Module - Orders Collection" );
    QUnit.test("Initialize the order collection", function () {
        expect( 1 );
        var orders = new Orders.OpenOrders();
        notEqual( orders.model, undefined, "Collection and associated Model initialized" );
    });

    // Open Order View
    module( "Order Module - OpenOrder View", {
        setup: function() {
            $( "body" ).append( "<div id='order-div'></div>" );
            this.orderView = new Orders.Views.OpenOrderView( "APPROVER" );
        },
        teardown: function() {
            $( "#order-div" ).remove();
            this.orderView.clearOrderTimer();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.orderView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.test("View has associated collection/model", function() {
        expect( 1 );
        notEqual( this.orderView.orders.model, undefined, "Collection and model are attached" );
    });
    QUnit.test("View has properly set url", function() {
        expect( 1 );
        equal( this.orderView.orders.url, ns.serviceURL + "/order/open?userId=1", "Collection URL properly set" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.orderView.render( function() {
            console.log(that.orderView.el);
            $( "#order-div" ).append( that.orderView.el );
            ok( $( "[data-role='listview']" ).length );
            start();
        });
    });
});
