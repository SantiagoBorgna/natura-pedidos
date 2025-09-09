# Natura Pedidos

Aplicación de escritorio desarrollada en **Java Swing** con conexión a **MySQL**, diseñada para facilitar la gestión de pedidos de representantes Natura.  
El sistema permite organizar y registrar los pedidos dentro de cada ciclo de ventas de manera clara y eficiente.

---

## Funcionalidades

- Gestión de ciclos: creación, edición y seguimiento de cada ciclo de ventas.  
- Registro de pedidos: carga de pedidos con datos de cliente, producto, cantidad y precio unitario.  
- Visualización centralizada: consulta de todos los pedidos organizados por ciclo.  
- Edición y eliminación de pedidos existentes.  

---

## Tecnologías utilizadas

- **Java Swing** → Interfaz gráfica de usuario.  
- **MySQL** → Base de datos relacional.  
- **JDBC** → Conexión entre Java y MySQL.  
- **Maven** → Gestión de dependencias y empaquetado del proyecto.  

---

## Instalación y ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/natura-pedidos.git
   
2. Crear la base de datos MySQL y las tablas necesarias:
  ```bash
  CREATE DATABASE natura_pedidos;
  
  USE natura_pedidos;
  
  CREATE TABLE ciclo (
      id INT AUTO_INCREMENT PRIMARY KEY,
      numero INT NOT NULL,
      fecha_inicio DATE NOT NULL,
      fecha_fin DATE NOT NULL
  );
  
  CREATE TABLE pedido (
      id INT AUTO_INCREMENT PRIMARY KEY,
      cliente VARCHAR(100) NOT NULL,
      producto VARCHAR(100) NOT NULL,
      codigo VARCHAR(50),
      cantidad INT NOT NULL,
      precio_unitario DECIMAL(10,2) NOT NULL,
      ciclo_id INT,
      FOREIGN KEY (ciclo_id) REFERENCES ciclo(id)
  );
```
3. Configurar la conexión en la clase DBConnection.java:
  ```bash
private static final String URL = "jdbc:mysql://localhost:3306/natura_pedidos";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_password";
```
4. Compilar y empaquetar el proyecto con Maven:
  ```bash
mvn clean package
  ```
5. Ejecutar el archivo .jar generado:
  ```bash
java -jar target/pedidos-app-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```
---
## Capturas de pantalla

<img width="1919" height="1138" alt="Captura de pantalla 2025-09-09 180327" src="https://github.com/user-attachments/assets/cf96a536-3d71-4853-a782-803dbdebe303" />
<img width="1919" height="1139" alt="Captura de pantalla 2025-09-09 180339" src="https://github.com/user-attachments/assets/1c01552c-eec8-42ab-bdfd-f65745a37540" />
<img width="1919" height="1138" alt="Captura de pantalla 2025-09-09 180350" src="https://github.com/user-attachments/assets/9ce5a19b-1351-4f9d-b680-43623b3a6519" />

---
## Licencia
Este proyecto está bajo la licencia MIT - ver el archivo LICENSE para más detalles.

---
## Contacto
Puedes contactarme via GitHub o email a **santiborgna5@gmail.com**.

