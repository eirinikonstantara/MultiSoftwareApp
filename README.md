# Image Upload and View API

A simple Spring Boot API that allows you to upload and view images. The application uses Azure SQL for metadata storage and Azure Blob Storage for image data storage.

## Features

- Upload images to Azure Blob Storage (in 'images-original' container)
- View images by ID
- List all available images
- Swagger UI for API documentation and testing

## Prerequisites

- Java 21
- Maven
- Azure SQL Database instance
- Azure Blob Storage account

## Getting Started

### Configuration

Update the `application.properties` file with your Azure details:

```properties
# Azure SQL Configuration
spring.datasource.url=jdbc:sqlserver://<your-server-name>.database.windows.net:1433;database=<your-database-name>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>

# Azure Blob Storage Configuration
azure.storage.connection-string=<your-storage-connection-string>
azure.storage.container.name=images-original
```

Alternatively, set the following environment variables:
- `AZURE_SQL_USERNAME`
- `AZURE_SQL_PASSWORD`
- `AZURE_STORAGE_CONNECTION_STRING`

### Building the Application

```bash
mvn clean package
```

### Running the Application

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Or using Maven:

```bash
mvn spring-boot:run
```

## API Endpoints

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### Available Endpoints:

- **POST /api/images/upload** - Upload an image file to Azure Blob Storage
- **GET /api/images/{id}** - Get an image by ID
- **GET /api/images** - Get metadata for all images

## Example Usage

### Upload an image:

```bash
curl -X POST -F "file=@/path/to/your/image.jpg" http://localhost:8080/api/images/upload
```

### View an image:

Open the following URL in your browser:
```
http://localhost:8080/api/images/{id}
```

### List all images:

```bash
curl http://localhost:8080/api/images
``` 