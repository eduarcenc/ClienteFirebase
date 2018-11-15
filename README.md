# Lista de datos en android utilizando Cloud Firestore [Firebase]

Aplicación cliente que solamente tiene permitido lectura de datos de la BD alojada en firebase.

<img src="https://github.com/jonathancplusplus/ClienteFirebase/blob/master/capturas/app_cliente.png" >


<b>Este proyecto tiene las siguientes caracteristicas: </b>

* Unicamente permitida la lectura de datos
* Lista de datos se actualiza automaticamente
* Implementación eficente dado que solo se actualizan los elementos modificados, NO toda la lista 
* Uso de Cloud Firestore en lugar de Realtime database por su eficiencia y facilidad
* En caso de que no se posea conexión a internet carga los ultimos datos descargados

<b>Estructura de la base de datos</b>

el tipo de BD que maneja firebase es no relacional (NoSQL) por lo que trabaja con coleciones y documentos, en este caso la colección utilizada en la app es <b>productos</b> y se puede observar que contiene una serie de documentos con id's generados automaticamente, y que ademas cada documentos posee una estructura siguiente:


La estructura de la BD es la siguiente
     
      ->productos              es la raiz
           -> 40YEcz..         es el id generado por defecto del documento
               -> nombre       nombre del producto
               -> precio       precio del producto
           -> Rjya4i5..        es el id generado por defecto del documento
               -> nombre       nombre del producto
               -> precio       precio del producto
          .
          .
          .
          Hasta N documentos bajo la coleccion productos

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


# Implementación

Para poder llamar a la coleción <b> productos </b>  alojada en Firebase es necesario haber generado el <b> google-services.json</b> correspondiente a nuestro proyecto y posteriormente agregar las librerias correspondientes, estos pasos estan detallados en la [Documentación oficial](https://firebase.google.com/docs/android/setup?hl=es-419).
