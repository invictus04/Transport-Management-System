# Transport Management System (TMS) - Backend

A robust backend service built with **Spring Boot** and **PostgreSQL** to manage the logistics of connecting Shippers with Transporters. This system handles complex business logic including capacity validation, optimistic locking for concurrent bookings, and a weighted algorithm for bid selection.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-grey)

## 🚀 Key Features

* **Load Management:** Shippers can post loads with specific truck requirements.
* **Transporter Management:** Transporters register fleets; capacity is tracked in real-time.
* **Bidding System:** Transporters bid on loads. The system validates fleet availability (Rule: `trucksOffered <= availableTrucks`) before accepting a bid.
* **Smart Booking:**
    * **Concurrency Control:** Uses `@Version` (Optimistic Locking) to prevent double-booking of loads.
    * **Inventory Management:** Automatically deducts trucks upon booking and restores them upon cancellation.
    * **Partial Allocation:** Supports multiple bookings per load until the total truck requirement is met.
* **Best Bid Algorithm:** auto-sorts bids based on a weighted formula:
    `Score = (1 / proposedRate) * 0.7 + (rating / 5) * 0.3`

---

## 🛠️ Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.2
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Validation:** Jakarta Validation
* **Mapping:** ModelMapper

---

## 🗄️ Database Schema

The database design ensures data integrity with proper Foreign Keys and Unique Constraints.

<img width="1014" height="787" alt="image" src="https://github.com/user-attachments/assets/174bb569-2b22-4157-baa6-8eedc431ae2b" />


**Core Entities:**
* **Load:** Tracks shipping requirements and status (`POSTED` -> `OPEN` -> `BOOKED`).
* **Transporter:** Tracks company details and live truck inventory.
* **Bid:** Links Transporters to Loads with price proposals.
* **Booking:** The final confirmed transaction.

---

## 🔌 API Documentation

### 1. Load APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/load` | Create a new load requirement. |
| `GET` | `/load` | Get all loads (Supports pagination & filtering by `shipperId`, `status`). |
| `GET` | `/load/{loadId}` | Get specific load details including active bids. |
| `PATCH` | `/load/{loadId}/cancel` | Cancel a load (Only if not BOOKED). |
| `GET` | `/load/{loadId}/best-bids` | Get bids sorted by the "Best Value" algorithm. |

### 2. Transporter APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/transporter` | Register a new transporter with truck capacity. |
| `GET` | `/transporter/{id}` | Get transporter details and available truck count. |
| `PUT` | `/transporter/{id}/trucks`| Update the fleet capacity. |

### 3. Bid APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/bid` | Submit a bid. **Validates capacity instantly.** |
| `GET` | `/bid` | Filter bids by `loadId`, `transporterId`, or `status`. |
| `GET` | `/bid/{bidId}` | Get bid details. |
| `PATCH` | `/bid/{bidId}/reject` | Reject a bid manually. |

### 4. Booking APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/booking` | Accept a bid. **Triggers truck deduction & optimistic lock.** |
| `GET` | `/booking/{id}` | Get booking details. |
| `PATCH` | `/booking/{id}/cancel` | Cancel booking and restore truck capacity to transporter. |

---

## 📝 Usage Examples

### 1. Submit a Bid
**POST** `/bid`
```json
{
    "loadId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "transporterId": "2e3b5f64-5717-4562-b3fc-2c963f66afa1",
    "proposedRate": 45000.0,
    "trucksOffered": 2
}
```

### 2. Create a Booking (Accept Bid)
**POST** `/booking`
```json
{
    "bidId": "8fe28e99-fb81-4a62-9040-83aed5081976"
}
```

### 3. Get Best Bids (Algorithm)
**GET** `/load/{loadId}/best-bids` Returns list sorted by Score (Price vs Rating)
```json
[
    {
        "bidId": "...",
        "transporterName": "FastLogistics",
        "proposedRate": 40000,
        "transporterRating": 4.8
    }
]
```

## ⚙️ Setup & Installation
### 1. Clone the repository
```bash
git clone https://github.com/yourusername/tms-backend.git
cd tms-backend
```

### 2. Configure Database Update 
`src/main/resources/application.properties` with your PostgreSQL credentials:
```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tms_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### 3. Run the Application
```bash
mvn spring-boot:run
```
### 4. Access API
The app runs on `http://localhost:8080`

