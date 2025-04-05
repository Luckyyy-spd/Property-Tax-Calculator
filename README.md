# Property Tax Calculator

## Overview
The Property Tax Calculator is a Java-based application that allows users to calculate property taxes based on various inputs such as property type, location, land fair market value (FMV), and improvement FMV. The application connects to a MySQL database to store and retrieve property records.

## Features
- Calculate property taxes based on user inputs.
- Store and retrieve property records from a MySQL database.
- Search and delete records from the database.
- User-friendly GUI built with Swing.

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java Development Kit (JDK) 8 or higher installed on your machine.
- MySQL Server installed and running.
- An IDE such as IntelliJ IDEA or Eclipse for Java development.

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/property-tax-calculator.git
   cd property-tax-calculator
   ```

2. **Set Up MySQL Database**
   - Create a new database named `property_tax_db` in your MySQL server.
   - Ensure that the database user has the necessary permissions to create tables and insert data.

3. **Configure Database Connection**
   - Open `src/machineprob/Database.java`.
   - Update the database connection details if necessary (username, password, etc.):
     ```java
     private static final String DB_URL = "jdbc:mysql://localhost:3306/";
     private static final String DB_NAME = "property_tax_db";
     private static final String USER = "root"; // Your MySQL username
     private static final String PASS = ""; // Your MySQL password
     ```

4. **Compile and Run the Application**
   - Open the project in your IDE.
   - Build the project to ensure all dependencies are resolved.
   - Run the `mainFrame` class to start the application.

## Usage
- Enter the property type, location, land FMV, and improvement FMV in the respective fields.
- Click the "Calculate" button to compute the property tax.
- Records can be saved to the database and viewed in the table.
- Use the search functionality to find specific records.
- Clear history or delete individual records as needed.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

