# 💻 API Rest en Java con conexión a base de datos MySQL

---

## 📋 Introducción
Tenemos una base de datos MySQL con dos tablas: 'Álbumes' y 'Canciones' (1-N). El objetivo es crear un API Rest en Java que permita hacer las operaciones CRUD en ambas tablas y algunas consultas más como mostrar cuántas canciones tiene cada álbum o buscar álbumes según año o intérprete.

---

## ⭐ Resultado
El resultado es un API Rest hecho en Java con las siguientes rutas:

### GET
- **/discografia/albumes** ➡️ muestra todos los álbumes
- **/discografia/albumes/{id}** ➡️ muestra el álbum con ese ID
- **/discografia/albumes/numtracks** ➡️ todos los álbumes y su número de canciones
- **/discografia/albumes/fecha/{año}** ➡️ todos los álbumes de dicho año
- **/discografia/albumes/interprete/{interprete}** ➡️ todos los álbumes del intréprete introducido
- **/discografia/canciones** ➡️ todas las canciones
- **/discografia/canciones/{id}** ➡️ la canción con el ID introducido
- **/discografia/canciones/interprete/{interprete}** ➡️ todas las canciones de dicho intérprete

### POST, PUT y DELETE
- Rutas POST y PUT: /discografia/albumes y /discografia/canciones
- Ruta DELETE: /discografia/albumes/{id} y /discografia/canciones/{id}
- En las tres operaciones se valida si el contenido con el que se quiere hacer la operación existe o no según el caso.