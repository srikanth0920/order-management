# Order Management

### Provides APIs to manage orders using JWT token based Authentication

## Getting an Access Token

### Sample Request

`POST http://localhost:4300/authenticate`

{
    "username": "admin",
    "password": "admin"
}

### Sample Response

{
    "accessToken": "eyBHVHJksdwfkjkkwkkndggBVNVFhj=",
    "refreshToken": "eyNJKBGGJKgkjhusfnjxkjskbNVBNV="
}

## Placing an Order

### Sample Request

`POST http://localhost:4300/orders`

{
    "orderType": "Electronics",
    "orderDescription": "Samsung TV"
}

### Sample Response

{
    "orderType": "Electronics",
    "orderDescription": "Samsung TV",
    "orderId": "1",
}
    

## Get list of all the orders

### Sample Request

`GET http://localhost:4300/orders`

### Sample Response

[{
    "orderType": "Electronics",
    "orderDescription": "Samsung TV",
    "orderId": "1",
}]



