# User Onboarding API

A secure RESTful API built with Spring Boot for user registration, authentication, and management with email verification and JWT-based token authentication.

## 🚀 Features

- **User Registration** - New user registration with email verification
- **Email Verification** - Account activation via email link
- **JWT Authentication** - Secure access tokens and refresh tokens
- **Token Refresh** - Automatic token rotation for enhanced security
- **Role-Based Access Control** - Support for USER and ADMIN roles
- **User Management** - CRUD operations for user accounts
- **Secure Logout** - Token invalidation on logout
- **Password Encryption** - BCrypt password hashing

## 🛠️ Tech Stack

- **Framework:** Spring Boot 2.7.18
- **Language:** Java 11
- **Database:** MySQL 8.0
- **Security:** Spring Security with JWT
- **Authentication:** JWT (JSON Web Tokens) using jjwt 0.12.5
- **Email Service:** Spring Mail (Gmail SMTP)
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Utilities:** Lombok

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 11** or higher
- **Maven 3.6+**
- **MySQL 8.0+** installed and running
- **Gmail Account** (for email verification service)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd UserUnboardingApi
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE user_onboarding_db;
```

### 3. Configure Application Properties

Copy the example properties file and update with your configuration:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Then update `src/main/resources/application.properties` with your configuration:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/user_onboarding_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Email Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Note:** For Gmail, you'll need to:
1. Enable 2-Factor Authentication
2. Generate an App Password (not your regular password)
3. Use the App Password in `spring.mail.password`

### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 📚 API Endpoints

### Public Endpoints (No Authentication Required)

#### Register User
```http
POST /public/register
Content-Type: application/json

{
  "userName": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response:**
```
Registration successfully created, please check your email for activation first
```

#### Verify Account
```http
GET /public/verify-account?token={verification_token}
```

**Response:**
```
Account Successfully Verified, You Can Now Log-in
```

#### Login
```http
POST /public/login
Content-Type: application/json

{
  "userName": "johndoe",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Authentication Endpoints

#### Refresh Access Token
```http
POST /auth/refresh-token
Content-Type: application/json
Authorization: Bearer {access_token}

{
  "refreshToken": "your_refresh_token_here"
}
```

**Response:**
```json
{
  "accessToken": "new_access_token",
  "refreshToken": "new_refresh_token"
}
```

### User Endpoints (Requires Authentication)

#### Logout
```http
DELETE /user/log-out
Authorization: Bearer {access_token}
```

**Response:**
```
logOut SuccessFull
```

#### Update User Profile
```http
PUT /user
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "userName": "newusername",
  "email": "newemail@example.com",
  "password": "newPassword123"
}
```

**Response:**
```json
{
  "id": 1,
  "userName": "newusername",
  "email": "newemail@example.com",
  "is_enabled": true,
  "role": "USER"
}
```

### Admin Endpoints (Requires ADMIN Role)

#### Create Admin Account
```http
POST /admin/create-admin
Content-Type: application/json

{
  "userName": "admin",
  "email": "admin@example.com",
  "password": "adminPassword123"
}
```

#### Get All Users
```http
GET /admin
Authorization: Bearer {admin_access_token}
```

#### Get User by Username
```http
GET /admin/{username}
Authorization: Bearer {admin_access_token}
```

#### Delete User
```http
DELETE /admin/{username}
Authorization: Bearer {admin_access_token}
```

## 🔐 Security Features

- **JWT Access Tokens** - Short-lived (1 hour) access tokens
- **Refresh Tokens** - Long-lived (7 days) refresh tokens with automatic rotation
- **Password Encryption** - BCrypt hashing for secure password storage
- **Email Verification** - Account activation required before login
- **Token Expiration** - Automatic token expiration and validation
- **Role-Based Authorization** - USER and ADMIN role separation

## 📁 Project Structure

```
UserUnboardingApi/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── sudarshangc/com/UserUnboardingApi/
│   │   │       ├── Config/
│   │   │       │   └── SpringSecurity.java          # Security configuration
│   │   │       ├── Controller/
│   │   │       │   ├── AdminController.java         # Admin endpoints
│   │   │       │   ├── AuthController.java         # Token refresh
│   │   │       │   ├── PublicController.java       # Public endpoints
│   │   │       │   └── UserController.java         # User endpoints
│   │   │       ├── DTO/
│   │   │       │   ├── LogOutRequest.java
│   │   │       │   ├── RegistrationRequest.java
│   │   │       │   ├── TokenRefreshRequest.java
│   │   │       │   └── TokenRefreshResponse.java
│   │   │       ├── Entity/
│   │   │       │   ├── RefreshToken.java            # Refresh token entity
│   │   │       │   ├── User.java                    # User entity
│   │   │       │   └── VerificationToken.java      # Email verification token
│   │   │       ├── Exception/
│   │   │       │   └── InvalidTokenException.java
│   │   │       ├── Filter/
│   │   │       │   └── JwtFilter.java               # JWT authentication filter
│   │   │       ├── Repository/
│   │   │       │   ├── RefreshTokenRepository.java
│   │   │       │   ├── UserRepository.java
│   │   │       │   └── VerificationTokenRepository.java
│   │   │       ├── Service/
│   │   │       │   ├── EmailService.java            # Email sending service
│   │   │       │   ├── RefreshTokenService.java     # Token management
│   │   │       │   ├── UserDetailsServiceImp.java   # User details service
│   │   │       │   ├── UserService.java             # User business logic
│   │   │       │   └── VerificationTokenService.java # Email verification
│   │   │       ├── Utils/
│   │   │       │   └── JwtUtils.java                # JWT utility methods
│   │   │       └── UserUnboardingApiApplication.java
│   │   └── resources/
│   │       └── application.properties               # Configuration
│   └── test/
│       └── java/
│           └── sudarshangc/com/UserUnboardingApi/
└── pom.xml
```

## 🔄 Authentication Flow

1. **Registration:**
   - User registers → Account created (disabled)
   - Verification email sent → User clicks link
   - Account enabled → User can login

2. **Login:**
   - User provides credentials → Authentication
   - Access token (1 hour) + Refresh token (7 days) issued

3. **Token Refresh:**
   - Access token expires → Use refresh token
   - New token pair issued → Old refresh token invalidated

4. **Logout:**
   - Refresh tokens deleted → User must login again

## 🧪 Testing

Run tests using Maven:

```bash
mvn test
```

## ⚠️ Important Notes

- **Security:** 
  - The current configuration has `.anyRequest().permitAll()` which allows all requests. For production, implement proper role-based security.
  - Never commit `application.properties` with real credentials to version control. Use `application.properties.example` as a template.
  - Consider using environment variables or Spring Cloud Config for sensitive configuration in production.
- **Email Configuration:** Ensure Gmail App Password is correctly configured (not your regular Gmail password)
- **Database:** The application uses `ddl-auto=update` which auto-creates/updates tables
- **Token Secret:** Consider using environment variables for JWT secret key in production

## 🐛 Known Issues

- `WebSecurityConfigurerAdapter` is deprecated (Spring Security 5.7+). Consider migrating to component-based security configuration.
- Some unused imports in the codebase (can be cleaned up).

## 📝 License

This project is open source and available under the MIT License.

## 👤 Author

**Sudarshan GC**

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

---


