package hv.dev4u.org.clientefirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdaptadorProductos extends ArrayAdapter<Producto> {

    public AdaptadorProductos(Context context, List<Producto> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Producto p = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_producto,parent,false);
        }

        TextView id     = convertView.findViewById(R.id.lbl_id_prod);
        TextView nombre = convertView.findViewById(R.id.lblNombreProducto);
        TextView precio = convertView.findViewById(R.id.lblPrecioProducto);
        ImageView imgProducto   = convertView.findViewById(R.id.imgItemProd);


        id.setText( position+1+"" );//muestro la posicion del elemento + 1 para que no empiece en cero
        nombre.setText( p.nombre );
        precio.setText( p.precio );


        if(p.getImagenProducto()!=null){
            imgProducto.setImageBitmap(p.getImagenProducto());
        }else{
            imgProducto.setImageResource(R.drawable.ic_producto);
        }


        return convertView;
    }
}
