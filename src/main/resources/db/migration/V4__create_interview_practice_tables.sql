CREATE TABLE departments (
                             dept_id INT PRIMARY KEY,
                             dept_name VARCHAR(50)
);

CREATE TABLE employees (
                           emp_id INT PRIMARY KEY,
                           emp_name VARCHAR(100),
                           dept_id INT,
                           salary INT,
                           hire_date DATE,
                           manager_id INT,
                           FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

CREATE TABLE orders (
                        order_id INT PRIMARY KEY,
                        emp_id INT,
                        order_amount DECIMAL(10,2),
                        order_date DATE,
                        FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);