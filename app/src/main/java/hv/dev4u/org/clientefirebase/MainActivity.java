package hv.dev4u.org.clientefirebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    /**
     *  La base de datos es de tipo: Cloud Firestore
     *  se ha puesto las siguientes reglas:
     *
     * service cloud.firestore {
     *   match /databases/{database}/documents {
     *     match /{document=**} {
     *       allow read, write;
     *     }
     *   }
     * }
     *
     *  La estructura de la BD es la siguiente
     *
     * ->productos              es la raiz
     *      -> Zxsd2312         es el id generado por defecto del documento
     *          -> nombre       nombre del producto
     *          -> precio       precio del producto
     *      -> Xcv123551        es el id generado por defecto del documento
     *          -> nombre       nombre del producto
     *          -> precio       precio del producto
     *     .
     *     .
     *     .
     *     Hasta N documentos bajo la coleccion productos
     */



    //elementos para la lista
    ListView listView;
    AdaptadorProductos adaptadorProductos;
    List<Producto> listaProductos;
    //base de datos firebase
    private CollectionReference dbProductos;

    public static StorageReference imgProductosFirebase;
    //imagen que se envia
    public static Bitmap imgSeleccionada=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializando controles para lista
        listView        = findViewById(R.id.listaProducto);
        listaProductos  = new ArrayList<>();
        adaptadorProductos = new AdaptadorProductos(this,listaProductos);
        listView.setAdapter(adaptadorProductos);
        //inicalizando la base de datos
        dbProductos     = FirebaseFirestore.getInstance().collection("productos");
        //imagenes
        imgProductosFirebase = FirebaseStorage.getInstance().getReference("imagenes_productos");

        //cada cambio en la bd se llama a actualizar datos
        dbProductos.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                actualizarDatos(queryDocumentSnapshots.getDocumentChanges());
            }
        });

    }

    private void actualizarDatos(List<DocumentChange> cambios){
        //para cada documento cambiado
        for(DocumentChange document_changed: cambios){
            //obtengo el documento
            DocumentSnapshot document = document_changed.getDocument();
            //obtengo la posicion del producto basado en el Id
            int posicion = posicionProducto(document.getId());
            //si el documento fue eliminado
            if(document_changed.getType()==DocumentChange.Type.REMOVED){
                //se elimina de la lista tambien
                listaProductos.remove(posicion);
            }else {
                //obtengo un objeto producto del documento
                final Producto producto = getProducto(document);
                //si la ruta no esta vacia entonces es por que apunta a una imagen
                if(producto.getRuta_imagen()!=null){
                    //preparo un megabyte para almacenar la imagen
                    final long ONE_MEGABYTE = 1024 * 1024;
                    imgProductosFirebase.child(producto.getRuta_imagen())
                            .getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    //descargo la imagen y la convierto a bitmap
                                    final int pos = posicionProducto(producto.getId_producto());
                                    Bitmap imagen = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    listaProductos.get(pos).setImagenProducto(imagen);
                                    //notifico de los cambios
                                    adaptadorProductos.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(MainActivity.this, "Ocurrio un error al descargar la imagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    // si no trae ruta por defecto se pone null a la imagen
                    producto.setImagenProducto(null);
                }

                //si la posicion es mayor a cero es por que existe en la lista y se actualiza
                if (posicion >= 0) {
                    listaProductos.set(posicion, producto);
                } else {
                    //si no , es por que es un elemento nuevo
                    listaProductos.add(producto);
                }
            }
            Log.d("LISTA FIREBASE","Actualizada "+document.getId()+ " "+document.getData().values());
        }
        //notifico al adaptador de los cambios
        adaptadorProductos.notifyDataSetChanged();
        //Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
    }

    //obtengo la posicion del producto basado en el id
    private int posicionProducto(String id){
        for(Producto producto : listaProductos) {
            if(producto.id_producto.equals(id)) {
                return listaProductos.indexOf(producto);
            }
        }
        return -1;
    }

    //obtengo objeto Producto basado en los parametros que hay en el documento de la BD
    private Producto getProducto(DocumentSnapshot doc){
        String id       = doc.getId();
        String nombre   = doc.getString("nombre");
        String precio   = doc.getString("precio");
        String ruta     = doc.getString("ruta_imagen");
        return new Producto(id,nombre,precio,ruta);
    }

}
