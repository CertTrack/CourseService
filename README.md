# Course Service

The **Course Service** is responsible for managing course-related operations within the platform. This includes functionality for course creation, retrieval, deletion, and module management.

## Key Features

- **Course Retrieval:** Fetch details of a course by ID, name, or category.
- **Course Creation:** Allow new courses to be created with a name, description, category, and modules.
- **Module Management:** Upload and retrieve course modules in `.zip` format.
- **Admin Operations:** Enable course deletion by administrators.
- **Secure Access:** Authentication and authorization with Spring Security.
- **Certificate Generation:** Generate and store course completion certificates.

## Technologies Used

- **Spring Boot**: Framework for building the service.
- **Spring Security**: Provides authentication and authorization capabilities.
- **Auth0 (JWT)**: For decoding JWT.
- **MongoDB**: NoSQL database for storing course details.
- **AWS S3**: For storing course modules.
- **Elasticsearch**: For efficient course searching.
- **Spring Cloud Eureka Client**

---

## Security

The service uses Spring Security to ensure endpoints are secured. All requests require authentication, and only authenticated users can interact with the endpoints.

---

## Endpoints

### General Endpoints

#### GET /courses/

**Description:** Retrieves a list of all available courses.

**Response:**

```json
[
  {
    "name": "Java Basics",
    "description": "Introduction to Java programming",
    "category": "Programming",
    "modules": ["module1.pdf", "module2.pdf"]
  },
  {
    "name": "Advanced Java",
    "description": "Deep dive into Java concepts",
    "category": "Programming",
    "modules": ["module1.pdf"]
  }
]
```

### Course-Specific Endpoints

#### GET /courses/id

**Description:** Retrieves details of a specific course by ID.

**Request Parameters:**

- `id` (int) - The unique ID of the course.

**Response:**

```json
{
  "name": "Java Basics",
  "description": "Introduction to Java programming",
  "category": "Programming",
  "modules": ["module1.pdf"]
}
```

#### GET /courses/name

**Description:** Retrieves a list of courses that match a specific name.

**Request Parameters:**

- `name` (String) - The name of the course.

#### GET /courses/category

**Description:** Retrieves a list of courses that belong to a specific category.

**Request Parameters:**

- `category` (String) - The category of the courses.

### Module Management Endpoints

#### GET /courses/downloadModule

**Description:** Downloads a specific module from a course.

**Request Parameters:**

- `courseId` (int) - The ID of the course.
- `moduleId` (int) - The ID of the module.

#### POST /courses/createCourse

**Description:** Creates a new course.

**Request Parameters:**

- `courseName` (String) - The name of the course.
- `courseDescription` (String) - The description of the course.
- `zipModule` (MultipartFile) - The ZIP archive containing course modules.

**Response:**

```json
{
  "message": "Successfully created"
}
```

#### POST /courses/addModule

**Description:** Adds a module to an existing course.

**Request Parameters:**

- `courseId` (int) - The ID of the course.
- `zipModule` (MultipartFile) - The ZIP archive containing the new module.

**Response:**

```json
{
  "message": "Successfully added module"
}
```

### Certificate Management Endpoints

#### POST /courses/generateCertificate

**Description:** Generates a certificate for a user who has completed a course.

**Request Parameters:**

- `userId` (int) - The ID of the user.
- `courseId` (int) - The ID of the completed course.

**Response:**

```json
{
  "message": "Certificate generated successfully",
  "certificateUrl": "https://s3.amazonaws.com/certificates/user123_course456.pdf"
}
```

#### GET /courses/certificate

**Description:** Retrieves the certificate for a user who has completed a course.

**Request Parameters:**

- `userId` (int) - The ID of the user.
- `courseId` (int) - The ID of the completed course.

**Response:**

```json
{
  "certificateUrl": "https://s3.amazonaws.com/certificates/user123_course456.pdf"
}
```

### Admin-Specific Endpoints

#### DELETE /courses/admin/delete

**Description:** Deletes a course by its ID (Admin only).

**Request Parameters:**

- `id` (int) - The unique ID of the course to delete.

**Response:**

```json
{
  "message": "Course successfully deleted"
}
```

---

## Database Schema

Courses are stored in a MongoDB collection `courses` with the following structure:

```json
{
  "id": "integer",
  "name": "string",
  "authorId": "integer",
  "description": "string",
  "category": "string",
  "modules": ["string"],
  "certificateTemplate": "string"
}
```

Certificates are stored in a separate collection `certificates`:

```json
{
  "id": "integer",
  "userId": "integer",
  "courseId": "integer",
  "certificateUrl": "string"
}
```

---

## How to Run

1. Clone the repository.
2. Navigate to the project directory.
3. Build and run the application using Maven or your preferred IDE.
4. The service will be available at `http://localhost:8082`.

---

## Notes

- Ensure the database is properly configured before running the service.
- Only authorized users can access admin endpoints.
- Course names and categories are case-sensitive.
- Elasticsearch integration allows efficient course searching.

---

## Future Enhancements

- Add support for updating course details.
- Implement course enrollment features.
- Introduce pagination for large course lists.

























































































































































































