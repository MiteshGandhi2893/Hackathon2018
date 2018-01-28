package Data;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miteshgandhi.mydietryapp.ItemDetails;
import com.example.miteshgandhi.mydietryapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import Model.Item;

/**
 * Created by miteshgandhi on 1/27/18.
 */
public class ItemRecycler extends RecyclerView.Adapter<ItemRecycler.ViewHolder>{

    String sku="";
    private Context context;
    private List<Item> itemList;

    public ItemRecycler(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ItemRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemrow,parent,false);


        return new ViewHolder(view,context);

    }

    @Override
    public void onBindViewHolder(ItemRecycler.ViewHolder holder, int position) {

        Item item=itemList.get(position);
        String Imagepath=item.getImagePath();
        sku=item.getSku();

        holder.desc.setText(item.getDescription());

        holder.brand.setText(item.getBrand());
        holder.product.setText(item.getProduct());

        Picasso.with(context).load(Imagepath).placeholder(android.R.drawable.ic_btn_speak_now).into(holder.itemImage);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView desc,brand,product,price;
        ImageView  itemImage;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context=ctx;


            desc=(TextView)itemView.findViewById(R.id.DescriptionId);
            brand=(TextView)itemView.findViewById(R.id.Brandid);
            product=(TextView)itemView.findViewById(R.id.productId);
            itemImage=(ImageView) itemView.findViewById(R.id.movieImage);
            price=(TextView) itemView.findViewById(R.id.PriceId);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item item=new Item();
                    item=itemList.get(getAdapterPosition());
                    Intent intent1=new Intent(context, ItemDetails.class);

                    intent1.putExtra("sku",sku);
                    intent1.putExtra("items",item);
                    ctx.startActivity(intent1);

                }
            });





        }


        @Override
        public void onClick(View view) {

        }
    }
}
