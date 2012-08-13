require([
    "namespace",
    "modules/catalog"
],
function( ns, Catalog ) {
    // Catalog Model
    module( "Catalog Module - Catalog Model" );
    QUnit.test("Initialize the catalog", function () {
        expect( 1 );
        var cat = new Catalog.FullCatalog();
        ok( true, "Model initialized" );
    });

    QUnit.asyncTest("Load catalog into localstorage", function () {
        expect( 1 );
        Catalog.buildCache().done( function() {
            var localCatalog = localStorage.getItem( "catalog" );
            notEqual( localCatalog, undefined, "Local storage initialized" );
            start();
        });
    });

    // Category View
    module( "Catalog Module - Category View", {
        setup: function() {
            $( "body" ).append( "<div id='catalog-div'></div>" );
            this.catView = new Catalog.Views.Categories();
        },
        teardown: function() {
            $( "#catalog-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.catView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.catView.render( function() {
            $( "#catalog-div" ).append( that.catView.el );
            ok( $( "[data-role='listview']" ).length );
            start();
        });
    });

    // Item List View
    module( "Catalog Module - ItemList View", {
        setup: function() {
            $( "body" ).append( "<div id='catalog-div'></div>" );
            this.catView = new Catalog.Views.ItemList( 1 );
        },
        teardown: function() {
            $( "#catalog-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.catView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.catView.render( function() {
            $( "#catalog-div" ).append( that.catView.el );
            ok( $( ".catalog-item" ).length );
            start();
        });
    });

    // Item Page View
    module( "Catalog Module - ItemPage View", {
        setup: function() {
            $( "body" ).append( "<div id='catalog-div'></div>" );
            this.catView = new Catalog.Views.ItemPage( 1, 2 );
        },
        teardown: function() {
            $( "#catalog-div" ).remove();
        }
    });
    QUnit.test("Associated DOM element", function() {
        expect( 1 );
        equal( this.catView.el.tagName.toLowerCase(), "div", "Default div created" );
    });
    QUnit.asyncTest("View can be rendered and is visible", function() {
        expect( 1 );
        var that = this;
        this.catView.render( function() {
            $( "#catalog-div" ).append( that.catView.el );
            ok( $( ".detailImage" ).length );
            start();
        });
    });
});
