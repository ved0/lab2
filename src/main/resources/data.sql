-- Add some categories
INSERT INTO categories(name, symbol, description)
VALUES ('Sunshine', '☀️', 'Kitchen you know'),
       ('Snow', '⛄', 'Something different');

-- Add some users
INSERT INTO users(username, first_name, last_name)
VALUES ('user1', 'John', 'Doe'),
       ('cool_kid', 'Sam', 'Smith');

-- Add some places
INSERT INTO places(name, category_id, added_by_user_id, description, public_or_private, coordinates)
VALUES ('Miami Beach', 1, 1, 'Swiming, taning and whatnot', 'private',
        ST_GeometryFromText('POINT(25.8237395 -80.1736198)', 4326)),
       ('Lomma Beach', 1, 2, 'Swiming, taning and whatnot', 'public',
        ST_GeometryFromText('POINT(55.682012 13.0377643)', 4326)),
       ('Åre', 2, 1, 'Skiing, cabins and whatnot', 'private',
        ST_GeometryFromText('POINT(63.3971649 13.0586557)', 4326)),
        ('Some Other place', 1, 2, 'Swiming, taning and whatnot', 'public',
        ST_GeometryFromText('POINT(43.682012 12.0377643)', 4326));
