# Lista en android utilizando Cloud Firestore

Aplicación cliente que solamente tiene permitido lectura de datos de la BD alojada en firebase.



<img src="https://github.com/jonathancplusplus/ClienteFirebase/blob/master/capturas/app_cliente.png" >



<b>Estructura de la base de datos</b>

<img src="https://github.com/jonathancplusplus/ClienteFirebase/blob/master/capturas/estructura_bd.png">

# IMPORTANTE:
El tipo de base de datos utilizada es <b> Cloud Firestore </b> y en las reglas se ha establecido como publicas con el siguiente código:

    service cloud.firestore {
      match /databases/{database}/documents {
        match /{document=**} {
          allow read, write;
        }
      }
    }
