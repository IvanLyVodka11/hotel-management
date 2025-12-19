# Class Diagram - Thành viên 2 (Booking, Customer, Invoice)

## Entity Relationships

```
┌─────────────────────┐         ┌──────────────────┐
│     Customer        │         │       Room       │
├─────────────────────┤         ├──────────────────┤
│ - customerId: String│         │ (Thành viên 1)   │
│ - fullName: String  │         └──────────────────┘
│ - email: String     │                 △
│ - phoneNumber: String               │
│ - idCard: String    │         ┌──────────────────┐
│ - address: String   │         │     Booking      │
│ - isVIP: boolean    │         ├──────────────────┤
│ - loyaltyPoints     │         │ - bookingId      │
│   double            │         │ - customer  ──────> (1)
│ + getters/setters   │         │ - room ──────────────> (1)
│ + addLoyaltyPoints()│         │ - checkInDate    │
└─────────────────────┘         │ - checkOutDate   │
        △                        │ - status         │
        │(1)                     │ - totalPrice     │
        │                        │ - notes          │
     (n)│                        │ + calculate...() │
        │                        └──────────────────┘
        │                                △
        │                             (1)│
        └────────────────────────────────┘
```

## Manager Classes

```
┌────────────────────────────────────────┐
│    IManageable<T> (Thành viên 1)      │
├────────────────────────────────────────┤
│ + add(T): void                         │
│ + update(T): void                      │
│ + delete(id: String): void             │
│ + getById(id: String): T               │
│ + getAll(): List<T>                    │
└────────────────────────────────────────┘
        △            △            △
        │            │            │
        │            │            │
┌───────┴──────┐ ┌──┴──────────┐ ┌───────┴───────┐
│ Customer     │ │ Booking     │ │ Invoice       │
│ Manager      │ │ Manager     │ │ Manager       │
├──────────────┤ ├─────────────┤ ├───────────────┤
│+ search()    │ │+ search()   │ │+ create...()  │
│+ filter()    │ │+ filter()   │ │+ getBy...()   │
│+ getVIP()    │ │+ isAvailable()│ + mark...()   │
│+ getTotal()  │ │+ getRevenue()│ │+ getRevenue() │
└──────────────┘ └─────────────┘ └───────────────┘
```

## Storage Layer

```
┌────────────────────────────┐
│    DataStorage             │
├────────────────────────────┤
│ - customerManager          │
│ - bookingManager           │
│ - invoiceManager           │
│ - roomManager              │
├────────────────────────────┤
│ + loadAllData()            │
│ + saveAllData()            │
│ + loadCustomers()          │
│ + saveCustomers()          │
│ + loadBookings()           │
│ + saveBookings()           │
│ + loadInvoices()           │
│ + saveInvoices()           │
│ - parseCustomer()          │
│ - parseBooking()           │
│ - parseInvoice()           │
│ - customerToJson()         │
│ - bookingToJson()          │
│ - invoiceToJson()          │
└────────────────────────────┘
        │       │       │
        │       │       └──────────> invoices.json
        │       └──────────────────> bookings.json
        └──────────────────────────> customers.json
```

## Invoice Status Flow

```
┌─────────────────────────────────────────────┐
│         Invoice Status Enum                 │
├─────────────────────────────────────────────┤
│ DRAFT ──> ISSUED ──> PAID                   │
│   │                    △                     │
│   └────> CANCELLED  ──┘                     │
└─────────────────────────────────────────────┘
```

## Booking Status Flow

```
┌──────────────────────────────────────────┐
│      Booking Status Enum                 │
├──────────────────────────────────────────┤
│ PENDING ──> CONFIRMED ──> CHECKED_IN     │
│    │           │              │           │
│    │           │              v           │
│    │           └──> COMPLETED             │
│    │                    △                 │
│    └──────> CANCELLED ──┘                 │
└──────────────────────────────────────────┘
```

## Method Call Flow: Creating a Booking

```
1. CustomerManager.getById(customerId)
   └─> Returns Customer object

2. RoomManager.getById(roomId) [Integration with Member 1]
   └─> Returns Room object

3. BookingManager.isRoomAvailable(room, checkIn, checkOut)
   └─> Checks for conflicts
   └─> Returns true/false

4. Booking booking = new Booking(...)
   ├─> Customer
   ├─> Room
   ├─> Dates
   └─> Calculates totalPrice

5. BookingManager.add(booking)
   └─> Adds to internal list

6. InvoiceManager.createInvoiceFromBooking(booking, invoiceId)
   ├─> Creates Invoice with booking
   ├─> Calculates subtotal, tax, total
   └─> Adds to internal list

7. DataStorage.saveAllData()
   ├─> saveBookings() → bookings.json
   └─> saveInvoices() → invoices.json
```

## Revenue Calculation Flow

```
Room Base Price (từ Member 1)
    │
    v
Number of Nights = checkOutDate - checkInDate
    │
    v
Room.calculatePrice(days) = basePrice * days * multiplier
    │
    v
Booking.totalPrice = Room.calculatePrice()
    │
    v
Invoice.subtotal = Booking.totalPrice
    │
    v
Invoice.taxAmount = subtotal * taxRate
    │
    v
Invoice.totalAmount = subtotal + taxAmount
    │
    v
InvoiceManager.getTotalRevenue()
    = sum of (invoice.totalAmount where status == PAID)
```

## Class Dependencies

```
Booking class dependencies:
├── Customer (composition)
├── Room (composition from Member 1)
├── LocalDate (Java)
├── BookingStatus (enum)
└── ChronoUnit (Java)

Invoice class dependencies:
├── Booking (composition)
├── LocalDate (Java)
├── InvoiceStatus (enum)
└── Customer (indirect via Booking)

Managers dependencies:
├── Collections (List, ArrayList)
├── Streams API (Java 8+)
├── Entity classes (Customer, Booking, Invoice)
└── Interfaces (IManageable, ISearchable)

DataStorage dependencies:
├── Gson (JSON library)
├── Entity classes
├── Manager classes
├── JsonObject, JsonArray
├── JsonParser
└── File I/O (BufferedReader, BufferedWriter)
```

## Key Features Implemented

### 1. Availability Checking
```java
// Prevents double-booking
isRoomAvailable(room, date1, date2) {
  for each booking with same room:
    if booking overlaps with dates:
      return false
  return true
}
```

### 2. Automatic Calculations
```java
// Booking total
Booking.calculateTotalPrice()
= Room.calculatePrice(numberOfDays)
= basePrice * days * roomMultiplier

// Invoice total
Invoice.getTotalAmount()
= subtotal + (subtotal * taxRate)
```

### 3. Revenue Tracking
```java
// Total revenue
getTotalRevenue() 
= sum of (invoice.totalAmount where PAID)

// Monthly revenue
getMonthlyRevenue(month, year)
= sum of revenues in specific month/year
```

### 4. Data Persistence
```java
// JSON Structure
customers.json: [
  { customerId, fullName, email, ... },
  ...
]

bookings.json: [
  { bookingId, customerId, roomId, ... },
  ...
]

invoices.json: [
  { invoiceId, bookingId, totalAmount, ... },
  ...
]
```

---

**Diagram Version**: 1.0
**Last Updated**: 15/12/2025
**Created for**: OOP Hotel Management Project
