INSERT INTO restaurants (id, name, description, image_url, address, rating, is_open)
VALUES
    (
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Tokyo Bento',
        'Japanese comfort food with fresh sushi and rice bowls.',
        'https://images.example.com/restaurants/tokyo-bento.jpg',
        '12 Sakura Street',
        4.7,
        TRUE
    ),
    (
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Pasta Fresca',
        'Classic Italian pasta and handmade sauces.',
        'https://images.example.com/restaurants/pasta-fresca.jpg',
        '48 Roma Avenue',
        4.5,
        TRUE
    ),
    (
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Green Bowl',
        'Healthy salads, wraps, and protein bowls.',
        'https://images.example.com/restaurants/green-bowl.jpg',
        '7 Healthy Lane',
        4.6,
        TRUE
    );

INSERT INTO menu_items (id, restaurant_id, name, description, price_cents, image_url, is_available)
VALUES
    (
        '39c8f6d7-fcd2-4c1d-8749-5c38b3b7baf2',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Salmon Nigiri Set',
        'Six pieces of salmon nigiri with wasabi.',
        1290,
        'https://images.example.com/menu/salmon-nigiri.jpg',
        TRUE
    ),
    (
        'b2c4318f-e7dc-45db-84e4-db4fc3a3db8f',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Chicken Katsu Bento',
        'Crispy chicken cutlet, rice, and pickled vegetables.',
        1450,
        'https://images.example.com/menu/chicken-katsu-bento.jpg',
        TRUE
    ),
    (
        '8f3e5c91-cc35-4ef5-a59c-0ce26f7b9d60',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Miso Ramen',
        'Pork broth ramen with corn and chashu.',
        1390,
        'https://images.example.com/menu/miso-ramen.jpg',
        TRUE
    ),
    (
        '093a3da4-df37-4598-9c6d-c9f5c5a37989',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Ebi Tempura',
        'Shrimp tempura with dipping sauce.',
        990,
        'https://images.example.com/menu/ebi-tempura.jpg',
        TRUE
    ),
    (
        '35b9cb0d-f555-4aa9-a4af-24c3fcf88397',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Matcha Cheesecake',
        'Creamy cheesecake flavored with ceremonial matcha.',
        650,
        'https://images.example.com/menu/matcha-cheesecake.jpg',
        TRUE
    ),
    (
        '2bb5b4ff-5b1f-4b77-bdfa-3771f8828389',
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Spaghetti Carbonara',
        'Traditional carbonara with egg yolk and pancetta.',
        1380,
        'https://images.example.com/menu/carbonara.jpg',
        TRUE
    ),
    (
        'ee66bcfd-f2f8-447d-90f0-35f69415f4ad',
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Penne Arrabbiata',
        'Spicy tomato sauce with garlic and chili.',
        1190,
        'https://images.example.com/menu/arrabbiata.jpg',
        TRUE
    ),
    (
        '3382f0fd-a875-47c1-a02f-5d8861f574f7',
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Mushroom Risotto',
        'Arborio rice with mushrooms and parmesan.',
        1490,
        'https://images.example.com/menu/mushroom-risotto.jpg',
        TRUE
    ),
    (
        '12f5c775-1fd6-4936-9471-aad51fca66f8',
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Margherita Pizza',
        'San Marzano tomato, mozzarella, and basil.',
        1290,
        'https://images.example.com/menu/margherita.jpg',
        TRUE
    ),
    (
        'f8f64661-18f8-45ff-a92b-3524e59f9649',
        '8da4c8cc-9f58-4ed0-9f7f-74d77f56e8aa',
        'Tiramisu',
        'Coffee-soaked ladyfingers and mascarpone cream.',
        690,
        'https://images.example.com/menu/tiramisu.jpg',
        TRUE
    ),
    (
        '39cff9da-f9dc-4f7f-b9dd-84eecf29bf7a',
        '6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274',
        'Edamame',
        'Steamed soybeans with sea salt.',
        490,
        'https://images.example.com/menu/edamame.jpg',
        TRUE
    ),
    (
        '834e9fd8-f857-4bcf-9481-8e20c18367d3',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Grilled Chicken Bowl',
        'Mixed greens, quinoa, grilled chicken, and avocado.',
        1420,
        'https://images.example.com/menu/grilled-chicken-bowl.jpg',
        TRUE
    ),
    (
        '8f99590b-457f-43df-9d4a-6b8f1516f17f',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Falafel Wrap',
        'Whole-wheat wrap with falafel and tahini sauce.',
        1090,
        'https://images.example.com/menu/falafel-wrap.jpg',
        TRUE
    ),
    (
        '05578c2f-4d10-4630-a7f2-3bcf4d96867f',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Caesar Salad',
        'Romaine lettuce, parmesan, and garlic croutons.',
        980,
        'https://images.example.com/menu/caesar-salad.jpg',
        TRUE
    ),
    (
        'a9e07303-6e44-44dd-a73c-281763f6918c',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Tofu Teriyaki Bowl',
        'Tofu, brown rice, broccoli, and teriyaki glaze.',
        1240,
        'https://images.example.com/menu/tofu-teriyaki-bowl.jpg',
        TRUE
    ),
    (
        'c44853f8-8471-4e9f-ab6c-c12f299ec52e',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Berry Yogurt Parfait',
        'Greek yogurt, granola, and seasonal berries.',
        610,
        'https://images.example.com/menu/berry-yogurt-parfait.jpg',
        TRUE
    ),
    (
        '1ab7f9f6-bb57-4282-9d11-7108f5fd8c2d',
        'c30d0f90-9cee-4db3-a772-4c5d3158f6ab',
        'Green Detox Smoothie',
        'Spinach, mango, banana, and coconut water.',
        720,
        'https://images.example.com/menu/green-detox-smoothie.jpg',
        TRUE
    );
