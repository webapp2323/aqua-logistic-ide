**Aqua Logistic Application**
This application is a web-based platform for managing logistics operations, including user authentication via OAuth2 and JWT, and data management using Spring Boot and Liquibase.


**Before setting up the project, ensure you have the following installed:**

Java 20
Maven
MySQL
Step-by-Step Setup

**Clone the Repository**
git clone https://github.com/your-repo/aqua-logistic.git

**Database Setup**

Create an empty database named aqua_logistic with the schema public.

**Configure Environment Variables**

In IntelliJ IDEA, go to Run > Edit Configurations, then set the following environment variables under the Environment Variables section:

FACEBOOK_CLIENT_ID=741221700959828
FACEBOOK_CLIENT_SECRET=53c913e6653b0876757359b525bbe2eb
GOOGLE_CLIENT_ID=835594044316-p7pir287c89td7nuq09bar0einrjuf6v.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=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
GITHUB_CLIENT_ID=3aa30fcfccc1c8d684b0
GITHUB_CLIENT_SECRET=b4118b9576cbfe0feeb9b794fad8569f
DB_URL=jdbc:mysql://water-app.c7uska2koirf.eu-north-1.rds.amazonaws.com:3306/water-app
DB_USERNAME=adee1a69-d669-4747-99ed-4d093cbf712e
DB_PASSWORD=password
MAIL_HOST=mail.prog.kiev.ua
MAIL_PORT=25
MAIL_USERNAME=zahirolek@gmail.com
MAIL_PASSWORD=your_mail_password
SERVER_PORT=8080

After that you will be able to start application using Spring boot

**Additional Features**
OAuth2 Login: Supports login via Google, Facebook, and GitHub.
JWT Authentication: Uses JWT for secure authentication.
Liquibase Integration: Manages database migrations seamlessly.
Spring Boot Security: Configured with Spring Security for robust security management.

**TODOs**
Set Up CI/CD: Integrate continuous integration and deployment pipelines.
Refine CORS Configuration: Update CORS settings for enhanced security.
Add More Entities: Expand the application with additional entities.
Add Tests: Cover the application with unit and integration tests.
