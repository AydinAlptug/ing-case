# Credit Module

## Overview

This module is responsible for managing the credit of customers. The system supports two types of users: admin and customer. Each customer has a unique credit limit and loan history with associated installments.
It is responsible for the following functionalities:

- Register a customer
- Login as a customer
- Login as an admin
- List the loans of a customer
- List the loan installments of a customer
- Create a loan for a customer
- Pay installment/s of a loan

## Data

### Users

Role |	Email| 	Password|	Customer ID
---|---|---|---
Admin |		admin@mail.com |		admin |		N/A
Customer 1	|customer@mail.com	|customer	|2b201da2-32cf-4d11-93d2-7336bb353cba
Customer 2	|customer2@mail.com	|customer |bc0cfb62-fedd-4863-9c1e-d57207b417af

### Loans and installments

Customer Name | Loan Amount | Total Loan Amount | Number of Installments | Start Date
--------------|-------------------|------------------------|------------|-----------
Customer 1    | 5000.00           | 5500.00           | 6                      | 01/2025
Customer 2    | 5000.00           | 5500.00           | 12                     | 10/2024

Customer | Total Loan Amount | Installment Amount | Paid Amount | Due Date    | Payment Date  | Status | Penalty
--------------|-------------------|--------------------|-------------|-------------|---------------|--------|---------
Customer 1    | 5500.00           | 916.67             | 0.00        | 01/01/2025  | N/A           | Unpaid | No     
Customer 1    | 5500.00           | 916.67             | 0.00        | 02/01/2025  | N/A           | Unpaid | No     
Customer 1    | 5500.00           | 916.67             | 0.00        | 03/01/2025  | N/A           | Unpaid | No     
Customer 1    | 5500.00           | 916.67             | 0.00        | 04/01/2025  | N/A           | Unpaid | No     
Customer 1    | 5500.00           | 916.67             | 0.00        | 05/01/2025  | N/A           | Unpaid | No     
Customer 1    | 5500.00           | 916.67             | 0.00        | 06/01/2025  | N/A           | Unpaid | No     
||                   |||||
Customer 2    | 5500.00           | 458.33             | 458.33      | 10/01/2024  | 10/01/2024    | Paid   | No     
Customer 2    | 5500.00           | 458.33             | 458.33      | 11/01/2024  | 11/01/2024    | Paid   | No     
Customer 2    | 5500.00           | 458.33             | 0.00        | 12/01/2024  | N/A           | Unpaid | Yes    
Customer 2    | 5500.00           | 458.33             | 0.00        | 01/01/2025  | N/A           | Unpaid | No    
Customer 2    | 5500.00           | 458.33             | 0.00        | 02/01/2025  | N/A           | Unpaid | No    
Customer 2    | 5500.00           | 458.33             | 0.00        | 03/01/2025  | N/A           | Unpaid | No    
Customer 2    | 5500.00           | 458.33             | 0.00        | 04/01/2025  | N/A           | Unpaid | No    
Customer 2    | 5500.00           | 458.33             | 0.00        | 05/01/2025  | N/A           | Unpaid | No    
Customer 2    | 5500.00           | 458.33             | 0.00        | 06/01/2025  | N/A           | Unpaid | No    
|         |                   |                    |             |             |               |        |        |

## Postman Colelctions
### [Credit Module.postman_collection.2.1.json](Credit%20Module.postman_collection.2.1.json)

### [Credit Module.postman_collection.v2.0.json](Credit%20Module.postman_collection.v2.0.json)

### [Publicly accessible postman collection](https://www.postman.com/spacecraft-astronaut-31521016/my-workspace/collection/247s782/credit-module )