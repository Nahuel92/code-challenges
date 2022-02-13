**Introducción**

El objetivo es construir un board de informes de estado del tiempo. Este servicio es multitenant, cada usuario puede definir uno o más boards, que expresan las locaciones que le interesan.

**Backend**

Existirán 2 recursos REST:

- API para agregar y remover locaciones a un board.
- API para acceder al estado actual o de un momento dado, de todas las locaciones del board.
- API para establecer una conexión continua, ya sea vía polling, o streaming (por ejemplo con un websocket). Esta conexión devuelve inicialmente el board completo, y en la medida en que cambia el estado del tiempo, se notifican las novedades a través del canal.

Ambos recursos (o los que se definan), serán relativos a un board. Por ejemplo: `/boards/martin/`

Cuando se registra el interés en una locación pasan dos cosas:

1. Se registra el interés en algún tipo de base de datos (redis, mongo, mysql, hsqldb o lo que quieras), de manera que estos intereses sobrevivan a un reinicio.
2. Un proceso recolector comienza a hacer polling sobre el servicio de tiempo y persiste las actualizaciones del tiempo. Tener especial cuidado de no exceder la cantidad máxima de requerimientos​ ​de​ ​tiempo​ ​(2000​ ​diarias).

El servicio de tiempo a usar es https://developer.yahoo.com/weather/
Cuando la aplicación arranca, releva todas las locaciones de todos los boards desde la base de datos y se suscribe (recomienza el polling) a todas.

**Front end**

Debe ser una aplicación React, AngularJS, Elm, iOS o Android (o nativa mediante ionic o React Native) que implemente los siguientes escenarios:

- CRUD para las locaciones de un board.
- Layout tipo lista para mostrar el board, actualizándose automáticamente en la medida en que llegan las novedades.