# Course Service

The **Course Service** is responsible for managing course-related operations within the platform. This includes functionality for course creation, retrieval, and deletion, as well as filtering courses by specific criteria such as name or category.

## Key Features

- **Course Retrieval:** Fetch details of a course by ID, name, or category.
- **Course Creation:** Allow new courses to be created with a name, description, category, and module count.
- **Admin Operations:** Enable course deletion by administrators.

## Technologies Used

- **Spring Boot**: Framework for building the service.
- **Java Persistence API (JPA)**: For database interactions.
- **RESTful API Design**: Provides a clean and structured API interface.
- **Validation Framework**: Ensures data integrity for course creation.

---

## Endpoints

### General Endpoints

#### `GET /courses/`
**Description:** Retrieves a list of all courses available.

**Response:**
```json
[
  {
    "name": "Java Basics",
    "description": "Introduction to Java programming",
    "category": "Programming",
    "module": 10
  },
  {
    "name": "Advanced Java",
    "description": "Deep dive into Java concepts",
    "category": "Programming",
    "module": 15
  }
]
```

### Course-Specific Endpoints

#### `GET /courses/id`
**Description:** Retrieves details of a specific course by ID.

**Request Parameters:**
- `id` (int) - The unique ID of the course.

**Response:**
```json
{
  "name": "Java Basics",
  "description": "Introduction to Java programming",
  "category": "Programming",
  "module": 10
}
```

#### `GET /courses/name`
**Description:** Retrieves a list of courses that match a specific name.

**Request Parameters:**
- `name` (String) - The name of the course.

**Response:**
```json
[
  {
    "name": "Java Basics",
    "description": "Introduction to Java programming",
    "category": "Programming",
    "module": 10
  }
]
```

#### `GET /courses/category`
**Description:** Retrieves a list of courses that belong to a specific category.

**Request Parameters:**
- `category` (String) - The category of the courses.

**Response:**
```json
[
  {
    "name": "Java Basics",
    "description": "Introduction to Java programming",
    "category": "Programming",
    "module": 10
  }
]
```

### Admin-Specific Endpoints

#### `POST /courses/`
**Description:** Creates a new course.

**Request Body:**
```json
{
  "name": "Spring Boot Basics",
  "description": "Learn the fundamentals of Spring Boot",
  "category": "Programming",
  "module": 8
}
```

**Response:**
```json
{
  "message": "course successfully uploaded"
}
```

#### `DELETE /courses/admin/delete`
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

## How to Run

1. Clone the repository.
2. Navigate to the project directory.
3. Build and run the application using Maven or your preferred IDE.
4. The service will be available at `http://localhost:8080`.

---

## Notes

- Ensure the database is properly configured before running the service.
- Only authorized users can access admin endpoints.
- Course names and categories are case-sensitive.

---

## Future Enhancements

- Add support for updating course details.
- Implement course enrollment features.
- Introduce pagination for large course lists.
