# Lista de datos en android utilizando Cloud Firestore [Firebase]

Aplicación cliente que solamente tiene permitido lectura de datos de la BD alojada en firebase.

<img src="https://github.com/jonathancplusplus/ClienteFirebase/blob/master/capturas/app_cliente.png" >


Este proyecto tiene las siguientes caracteristicas:

* Unicamente permitida la lectura de datos
* Lista de datos se actualiza automaticamente
* Implementación eficente dado que solo se actualizan los elementos modificados, NO toda la lista 
* Uso de Cloud Firestore en lugar de Realtime database por su eficiencia y facilidad
* En caso de que no se posea conexión a internet carga los ultimos datos descargados

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
