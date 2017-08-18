package in.trainhopper.trainhopper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class SelectedOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Date date;

    SelectedOptionAdapter() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            date= simpleDateFormat.parse(MainActivity.date);
            date=new Date(date.getTime()+(SelectedOption.element.legs.get(0).arrival_start*1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        if(viewType==0)
            return new ViewHolder(layoutInflater.inflate(R.layout.result_details_card,parent,false));
        else
            return new ViewHolder2(layoutInflater.inflate(R.layout.layover_details_card,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderTemp, int position) {
       try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            if (SelectedOption.element.legs.get(position) == null) {
                ViewHolder2 holder=(ViewHolder2)holderTemp;
                holder.textView2.setText(SelectedOption.element.layover / 3600 + ":" + (((SelectedOption.element.layover / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.layover / 60 % 60 + " HRS");
                Date date1=new Date(date.getTime()+(SelectedOption.element.layover*1000));
                date=date1;
            } else {
                ViewHolder holder=(ViewHolder)holderTemp;
                holder.textView11.setText(SelectedOption.element.legs.get(position).station_id_start);
                holder.textView10.setText(SelectedOption.element.legs.get(position).station_id_end);

                holder.textView9.setText(SelectedOption.element.legs.get(position).train_name);
                holder.textView8.setText(SelectedOption.element.legs.get(position).train_id + "");
                holder.textView7.setText(SelectedOption.element.legs.get(position).train_class);

                holder.textView6.setText(SelectedOption.element.legs.get(position).station_name_start);
                holder.textView5.setText(SelectedOption.element.legs.get(position).station_name_end);

                Date dateObj = sdf.parse(SelectedOption.element.legs.get(position).arrival_start / 3600 + ":" + (((SelectedOption.element.legs.get(position).arrival_start / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).arrival_start / 60 % 60);
                holder.textView4.setText(new SimpleDateFormat("h:mm a").format(dateObj));

                SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("MMMM dd, yyyy");

                holder.textView13.setText(simpleDateFormat1.format(date));

                dateObj = sdf.parse(SelectedOption.element.legs.get(position).arrival_end / 3600 + ":" + (((SelectedOption.element.legs.get(position).arrival_end / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).arrival_end / 60 % 60);
                holder.textView3.setText(new SimpleDateFormat("h:mm a").format(dateObj));

                holder.textView.setText(SelectedOption.element.legs.get(position).duration / 3600 + ":" + (((SelectedOption.element.legs.get(position).duration / 60 % 60) < 10) ? "0" : "") + SelectedOption.element.legs.get(position).duration / 60 % 60 + " HRS");

                Date date1=new Date(date.getTime()+(SelectedOption.element.legs.get(position).duration*1000));
                date=date1;
                holder.textView17.setText(simpleDateFormat1.format(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return SelectedOption.element.legs.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView,textView2,textView3,textView4
                ,textView5,textView6,textView7,textView8
                ,textView9,textView10,textView11,textView12
                ,textView13,textView17;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView15);
            textView3= itemView.findViewById(R.id.textView14);
            textView4 = itemView.findViewById(R.id.textView19);
            textView5 = itemView.findViewById(R.id.textView26);
            textView6 = itemView.findViewById(R.id.textView27);
            textView7= itemView.findViewById(R.id.textView20);
            textView8 = itemView.findViewById(R.id.textView16);
            textView9 = itemView.findViewById(R.id.textView18);
            textView10 = itemView.findViewById(R.id.textView11);
            textView11= itemView.findViewById(R.id.textView12);
            textView12= itemView.findViewById(R.id.textView38);
            textView13= itemView.findViewById(R.id.textView13);
            textView17= itemView.findViewById(R.id.textView17);


        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder{

        TextView textView,textView2;

        ViewHolder2(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView34);
            textView2 = itemView.findViewById(R.id.textView35);
        }
    }
}
