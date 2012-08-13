define(
    [
        "jquery",
        "mockjax"
    ],
    function( $, mockjax ) {
        // Main Module
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/user",
            type: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: {
                "id": "1",
                "name": "test - 1"
            }
        });
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/user/*",
            type: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: {
                "id": "1",
                "name": "test - 1"
            }
        });
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/user/*",
            type: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: {
                "id": "1",
                "name": "test - 1",
                "team": "WEST",
                "role": "BUYER",
                "buyerTotal": {
                    "bought": 0.00,
                    "approved": 0.00,
                    "rejected": 0.00,
                    "awaitingApproval": 0.00
                },
                "approverTotal": null
            }
        });

        //Catalog Module
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/category",
            type: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: {"1":{"id":"1","name":"Tablets","items":{"2":{"id":"2","category":{"id":"1","name":"Tablets"},"name":"Samsung Galaxy Tab 2","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","description":"Take your media on the go with this Samsung Galaxy Tab 2 7.0 that features the Android 4.0 Ice Cream Sandwich operating system and 1GB RAM for powerful computing.","price":249.99},"3":{"id":"3","category":{"id":"1","name":"Tablets"},"name":"Apple iPad (newest model) 16GB Wifi","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/ipad_white.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/ipad_white.jpg","description":"9.7 in Retina display with Multi-Touch; Dual-core A5X chip with quad-core graphics; 5.0MP iSight camera and FaceTime camera; 1080p HD video recording","price":499.99},"4":{"id":"4","category":{"id":"1","name":"Tablets"},"name":"Amazon Kindle Fire","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/kindle_fire.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/kindle_fire.jpg","description":"7 in color multi-touch display; Wi-Fi capability, multi-touch display supports Kindle (AZW), TXT, PDF, unprotected MOBI, PRC natively, Audible, DOC, DOCX, JPG, GIF, PNG, BMP, non-DRM AAC, MP3, MIDI, OGG, WAV, MP4, VP8 formats","price":199.99}}},"5":{"id":"5","name":"Phones","items":{"6":{"id":"6","category":{"id":"5","name":"Phones"},"name":"Samsung Galaxy Nexus","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_nexus.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_nexus.jpg","description":"Android 4.0, Wifi, 4.7 in Super AMOLED touch-screen","price":799.99},"7":{"id":"7","category":{"id":"5","name":"Phones"},"name":"Apple iPhone 4S","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/iphone.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/iphone.jpg","description":"iOS 5, Siri voice assistance, iCloud, iSight 8.0MP with 1080p HD video, 16GB","price":699.99},"8":{"id":"8","category":{"id":"5","name":"Phones"},"name":"HTC One S","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/htc_one.gif","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/htc_one.gif","description":"HTC One X S720E with Beats Audio Unlocked GSM Android SmartPhone","price":599.99}}},"9":{"id":"9","name":"Gear","items":{"10":{"id":"10","category":{"id":"9","name":"Gear"},"name":"jBPM Golf Shirt","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/jbpm_golf_shirt.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/jbpm_golf_shirt.jpg","description":"Tee off in casual style. Our pique knit golf t-shirt is a comfortable, lightweight way to play 18-holes and beat the heat. Features, stylish white pearl buttons, yet it feels like wearing your favorite t-shirt. Dress it up or down. Throw a blazer over later for country club mingling. Great for layering.","price":18.99},"11":{"id":"11","category":{"id":"9","name":"Gear"},"name":"Aerogear Sigg Water Bottle","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/aerogear_sigg_water_bottle.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/aerogear_sigg_water_bottle.jpg","description":"Help save the planet while you rehydrate in style with an eye-catching water bottle from SIGG. Made from a single piece of aluminum, it's ultra-lightweight yet rugged and crack-resistant. To minimize unwanted tastes and scents, the inside is lined with a water-based, non-toxic epoxy resin that is compliant with FDA anti-leaching requirements.","price":24.99},"12":{"id":"12","category":{"id":"9","name":"Gear"},"name":"Errai Hoodie","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/errai_hoodie.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/errai_hoodie.jpg","description":"Stay warm on the inside. Look oh-so-cool on the outside. Don this comfortable fleece sweatshirt for that dress-down BBQ -- or your next dress-to-impress trip to the mall.","price":34.99},"13":{"id":"13","category":{"id":"9","name":"Gear"},"name":"Arquillian Button (10 Pack)","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/arquillian_225_button_10_pack.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/arquillian_225_button_10_pack.jpg","description":"Wear and share a favorite design or saying with 10 of your closest friends. Pass 'em out at parties, give 'em away at Burning Man. Keep 'em all for yourself and make some unique wall art. Collect 'em, trade 'em.","price":13.99},"14":{"id":"14","category":{"id":"9","name":"Gear"},"name":"Code Thug Sleeveless Tee","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/code_thug_mens_sleeveless_tee.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/code_thug_mens_sleeveless_tee.jpg","description":"Beat the summer heat or workout in comfort. Our 100% cotton sleeveless tee from Anvil is what to wear when you want to go casually cool.","price":15.99}}}}
        });

        //Cart Module
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/cart/*",
            type: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: {"id":"1","orderItems":[{"id":"999","item":{"id":"2","category":{"id":"1","name":"Tablets"},"name":"Samsung Galaxy Tab 2","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","description":"Take your media on the go with this Samsung Galaxy Tab 2 7.0 that features the Android 4.0 Ice Cream Sandwich operating system and 1GB RAM for powerful computing.","price":249.99},"quantity":1}]}
        });

        //Order Collection
        $.mockjax({
            url: "/git-repo/jboss-keynote-2012/client/src/test/qunit/order/open*",
            type: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            responseText: [{"id":"1","buyer":{"id":"111","name":"test - 1111"},"approved":null,"rejectionMessage":null,"orderItems":[{"id":"999","item":{"id":"2","category":{"id":"1","name":"Tablets"},"name":"Samsung Galaxy Tab 2","imageURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","thumbnailURL":"http://keynote-jbossworld.rhcloud.com/jbossworld/images/samsung_galaxy_tab_2.jpg","description":"Take your media on the go with this Samsung Galaxy Tab 2 7.0 that features the Android 4.0 Ice Cream Sandwich operating system and 1GB RAM for powerful computing.","price":249.99},"quantity":1}],"exceedsLimit":false,"signedOff":null,"approver":null,"assignee":null,"total":249.99}]
        });
    }
);
