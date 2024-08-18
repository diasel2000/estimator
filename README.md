# Estimator

Estimator is an application designed to estimate the cost of projects based on utilized technologies, man-hours, risk factors, and a variety of other parameters.

## Description

Estimator allows users to evaluate project costs based on multiple inputs such as:

- Utilized technologies
- Man-hours
- Risk factors
- Other variables impacting the final cost

This application assists project managers, developers, and clients in obtaining accurate project cost estimates during the early planning stages.

## Key Features

- Project cost estimation based on user-provided parameters
- Consideration of risks and their impact on costs
- Flexible settings for different project types
- Intuitive and user-friendly interface
- Ability to save and export estimates

## System Requirements

- Java 11+
- MySQL 8.0+
- Gradle 7.0+
- Payara Server 5.2020+
- Docker 20.10+

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/estimator.git
    cd estimator
    ```

2. Set up the MySQL database:

    ```sql
    create table permissions
(
    permissionid    int          not null,
    PermissionName  varchar(50)  not null,
    Description     text         null,
    permission_name varchar(255) not null,
    constraint PermissionID
        unique (permissionid),
    constraint PermissionName
        unique (PermissionName),
    constraint UK_nry1f3jmc4abb5yvkftlvn6vg
        unique (permission_name)
);

alter table permissions
    add primary key (permissionid);

create table roles
(
    roleid      int          not null,
    RoleName    varchar(50)  not null,
    Description text         null,
    role_name   varchar(255) not null,
    constraint RoleID
        unique (roleid),
    constraint RoleName
        unique (RoleName),
    constraint UK_716hgxp60ym1lifrdgp67xt5k
        unique (role_name)
);

alter table roles
    add primary key (roleid);

create table rolepermissions
(
    roleid       int not null,
    permissionid int not null,
    primary key (roleid, permissionid),
    constraint FKh6msuhq21l6d2g7dbvgv19ubl
        foreign key (permissionid) references permissions (permissionid),
    constraint FKq336qmixawe286d6aohndh2ym
        foreign key (roleid) references roles (roleid)
);

create table subscriptions
(
    subscriptionid    int            not null,
    Description       text           null,
    Price             decimal(10, 2) not null,
    subscription_name varchar(255)   not null,
    constraint SubscriptionID
        unique (subscriptionid)
);

alter table subscriptions
    add primary key (subscriptionid);

create table users
(
    userid         int auto_increment,
    Username       varchar(50)                         not null,
    Email          varchar(100)                        not null,
    GoogleID       varchar(100)                        not null,
    CreatedAt      timestamp default CURRENT_TIMESTAMP null,
    subscriptionid int                                 null,
    created_at     datetime                            not null,
    password       varchar(255)                        not null,
    constraint Email
        unique (Email),
    constraint GoogleID
        unique (GoogleID),
    constraint UK_r53o2ojjw4fikudfnsuuga336
        unique (password),
    constraint UserID
        unique (userid),
    constraint Username
        unique (Username),
    constraint FK_users_subscriptions
        foreign key (subscriptionid) references subscriptions (subscriptionid)
);

alter table users
    add primary key (userid);

create table userroles
(
    userid int not null,
    RoleID int not null,
    primary key (userid, RoleID),
    constraint FK4n5hrit2bxugxfomumvnlq3fs
        foreign key (userid) references users (userid),
    constraint FKa3r9sw47fs8gyju9wqy5nr2ip
        foreign key (RoleID) references roles (roleid)
);
    ```

3. Configure the `application.properties` file for database connection:

    ```properties
    spring.application.name=estimator
spring.main.allow-bean-definition-overriding=true
server.port=8081

spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

jwt.expiration=3600000

logging.level.org.springframework=DEBUG
logging.level.com.estimator=DEBUG

spring.datasource.url=jdbc:mysql://localhost:3306/estimator
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile, email
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8081/login/oauth2/code/{registrationId}
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo
spring.security.oauth2.client.provider.google.user-name-attribute=sub


sentry.dsn=${SENTRY_DSN}
sentry.release=0.0.1
#spring.profiles.active=prod
#sentry.environment=production
spring.profiles.active=dev
sentry.environment=development
sentry.minimum.level=warn
sentry.debug=true
    ```

4. Build and run the application:

    ```sh
    ./gradlew build
    ./gradlew bootRun
    ```

## Usage

1. Open your browser and navigate to `http://localhost:8080`.
2. Create a new project, add parameters, and obtain a cost estimate.

