db.createCollection('user_info');

db.user_info.insert({
    "firstName": "User",
    "lastName": "Automatic",
    "email": "user@email.com",
    "status": "active"
});

db.user_info.createIndex({
    'status': 1
});