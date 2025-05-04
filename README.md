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

This application uses environment variables for configuration. You can set them directly in your environment or use a .env file with a tool like spring-dotenv.

Required environment variables:

```
# Azure SQL Configuration
AZURE_SQL_URL=jdbc:sqlserver://<your-server>.database.windows.net:1433;database=<your-db>;encrypt=true
AZURE_SQL_USERNAME=your_username
AZURE_SQL_PASSWORD=your_password

# Azure Blob Storage
AZURE_STORAGE_CONNECTION_STRING=your_storage_connection_string
AZURE_STORAGE_CONTAINER_NAME=images-original

# Optional
PORT=8080  # Default is 8080, but Azure Web Apps expects 80
```

For local development, you can use the default values in application.properties.

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

## Deployment to Azure

### App Service Configuration

1. In Azure App Service, set these environment variables in Configuration → Application Settings:
   - AZURE_SQL_URL
   - AZURE_SQL_USERNAME
   - AZURE_SQL_PASSWORD
   - AZURE_STORAGE_CONNECTION_STRING
   - AZURE_STORAGE_CONTAINER_NAME
   - PORT=80

2. Set the startup command in Configuration → General settings:
   ```
   java -jar /home/site/wwwroot/app.jar
   ```

3. Upload the JAR file to /home/site/wwwroot/ via Kudu Console (Advanced Tools)

4. Make sure the blob container has correct public access settings for the images to be accessible.

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