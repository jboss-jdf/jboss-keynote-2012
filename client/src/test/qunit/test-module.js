// Set the require.js configuration
require.config({
    paths: {
        // JavaScript folders
        libs: "../../main/webapp/assets/js/libs",
        plugins: "../../main/webapp/assets/js/plugins",
        modules: "../../main/webapp/app/modules",
        templates: "../../main/webapp/app/templates",
        testModules: "test-modules",

        // Libraries
        jquery: "../../main/webapp/assets/js/libs/jquery.min",
        jquerym: "../../main/webapp/assets/js/libs/jquery.mobile-1.1.0.min",
        lodash: "../../main/webapp/assets/js/libs/lodash",
        backbone: "../../main/webapp/assets/js/libs/backbone",
        qunit: "qunit",
        mockjax: "jquery.mockjax",
        mocks: "mocks",

        // Plugins
        use: "../../main/webapp/assets/js/plugins/use",
        text: "../../main/webapp/assets/js/plugins/text",

        // Helpers
        namespace: "../../main/webapp/app/namespace"
    },

    use: {
        backbone: {
            deps: ["use!lodash", "jquery"],
            attach: "Backbone"
        },

        lodash: {
            attach: "_"
        },
        qunit: {
            attach: "QUnit"
        }
    }
});

define(
    [
        "namespace",
        "jquery",
        "use!backbone",
        "test",
        "use!qunit",
        "use!mockjax",
        "mocks"
    ],
    function ( ns, $, Backbone, test, QUnit, mockjax, mocks ) {
        // Set this url to your local test path
        ns.serviceURL = "/git-repo/jboss-keynote-2012/client/src/test/qunit";

        return {
            RunTests: function () {
                require(
                    [
                        "testModules/main",
                        "testModules/catalog",
                        "testModules/cart",
                        "testModules/order"
                    ]
                );
            }
        };
    }
);
