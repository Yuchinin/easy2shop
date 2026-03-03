Easy2Shop is a simple Android-based online shopping application built as a learning project to get familiar with Android development. It demonstrates how sellers can upload products for sale and buyers can browse available items.


📱 Project Overview
Platform: Android (developed using Eclipse IDE)
Language: Java
Backend: Apache server hosting RESTful APIs
Database: MySQL for product and user data storage
Architecture: Client–Server model

✨ Features

Buyer Side
Browse products uploaded by sellers
View product details (name, description, price, image)
Add products to cart
Simple checkout flow (demo purpose)

Seller Side
Upload new products with details and images
Manage product listings (edit or delete)
View buyer interest in products

⚙️ Technical Details
Android App:  
Built in Java using Eclipse IDE. The app communicates with the backend via HTTP requests to fetch and update product data.

Backend (XAMPP):  
The server is hosted using XAMPP, which provides:
Apache for serving PHP-based API endpoints
MySQL for database storage
PHP scripts to handle CRUD operations for products, users, and orders

Database:  
MySQL database managed through XAMPP’s phpMyAdmin interface. Tables include:
users (seller/buyer accounts)
products (product details, images, prices)
orders (basic order tracking)


🎯 Purpose
This project is intended as a learning showcase for Android development and full-stack integration:
Understanding Android UI and activity lifecycle
Working with REST APIs
Connecting mobile apps to server-side databases
Building a simple e-commerce workflow
