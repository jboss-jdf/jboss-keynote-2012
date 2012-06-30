// Set the require.js configuration
require.config({
    // Initialize the application with the main application file
    deps: ["main"],

    paths: {
        // JavaScript folders
        libs: "../assets/js/libs",
        plugins: "../assets/js/plugins",

        // Libraries
        jquery: "../assets/js/libs/jquery.min",
        jquerym: "../assets/js/libs/jquery.mobile-1.1.0.min",
        lodash: "../assets/js/libs/lodash",
        backbone: "../assets/js/libs/backbone",

        // Plugins
        use: "../assets/js/plugins/use",
        text: "../assets/js/plugins/text"
    },

    use: {
        backbone: {
            deps: ["use!lodash", "jquery"],
            attach: "Backbone"
        },

        lodash: {
            attach: "_"
        }
    }
});
