require([
    "namespace",
    "modules/main"
],
function( ns, Main ) {
    // User Model
    module( "Main Module - User Model" );
    QUnit.test("Initialize a User", function () {
        expect( 1 );
        var user = new Main.User();
        equal( user.urlRoot, ns.serviceURL + "/user", "Default URL Root set" );
    });

    // User registration View
    module( "Main Module - Registration View", {
        setup: function() {
            $( "body" ).append( "<div id='user-div'></div>" );
            this.regView = new Main.Views.Registration( "BUYER" );
        },
        teardown: function() {
            $( "#user-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.regView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.test("Has a model instance", function() {
        expect( 2 );
        notEqual( this.regView.model, undefined, "Model defined" );
        equal( this.regView.model.urlRoot, ns.serviceURL + "/user", "Model URL defined" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.regView.render( function() {
            $( "#user-div" ).append( that.regView.el );
            ok( $( "#profileForm" ).length, "View rendered - form found" );
            start();
        });
    });

    // User role View
    module( "Main Module - Role View", {
        setup: function() {
            var that = this;
            $( "body" ).append( "<div id='user-div'></div>" );
            this.regView = new Main.Views.Registration( "BUYER" );
            this.regView.model.save( {"id":"1","name":"test - 1","role":"BUYER","team":"WEST"});
            this.roleView = new Main.Views.Role();
        },
        teardown: function() {
            $( "#user-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.roleView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.test("Has a model instance", function() {
        expect( 2 );
        notEqual( this.roleView.model, undefined, "Model defined" );
        equal( this.roleView.model.urlRoot, ns.serviceURL + "/user", "Model URL defined" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.roleView.render(function() {
            $( "#user-div" ).append( that.roleView.el );
            ok( $( "#user-div" ).find( ".order-summary" ).length, "View rendered - order summary found" );
            start();
        });
    });
});
