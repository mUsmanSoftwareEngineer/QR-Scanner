package scanner.app.scan.qrcode.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import scanner.app.scan.qrcode.reader.R;
import scanner.app.scan.qrcode.reader.data.preference.GenerateModel;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder> {

    Context context;
    List<GenerateModel> generateModelList;

    private ClickListener clickListener;




    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_recycler, parent, false);
        return new BarcodeViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(BarcodeViewHolder holder, int position) {

        holder.generateImageView.setImageResource(generateModelList.get(position).getImg_icon());
        holder.generateTextView.setText(generateModelList.get(position).getCategory_name());



    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return generateModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class BarcodeViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout generateRelative;
        ImageView generateImageView;
        TextView generateTextView;

        public BarcodeViewHolder(View itemView,int viewType) {
            super(itemView);

            generateRelative=itemView.findViewById(R.id.anotherRecycler);
            generateImageView=itemView.findViewById(R.id.generate_txt_img);
            generateTextView=itemView.findViewById(R.id.generate_text);

            generateRelative.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClicked(getLayoutPosition());
                }
            });

        }
    }

    public interface ClickListener {

        void onItemClicked(int position);

    }

    public BarcodeAdapter(Context context, List<GenerateModel> generateModelList) {
        this.context = context;
        this.generateModelList = generateModelList;
    }
}
