package in.trainhopper.trainhopper;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder> {

    // --Commented out by Inspection (19-08-2017 05:35 PM):private Activity context;
    private ArrayList<ResultContainer> resultElements = new ArrayList<>();

    ResultRecyclerViewAdapter(Activity context, ArrayList<ResultContainer> resultElements) {
        this.resultElements = resultElements;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.result_card,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id = position;
        ResultContainer resultContainer = resultElements.get(holder.id);
        try {
            if (resultContainer.legs.size() == 1) {
                holder.linearLayout.setVisibility(View.GONE);
                holder.textView.setVisibility(View.GONE);
                holder.textView6.setText(resultContainer.legs.get(0).train_name);
            } else {
                resultContainer.layover = resultContainer.total_duration
                        - (resultContainer.legs.get(0).duration + resultContainer.legs.get(1).duration);

                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.VISIBLE);
                holder.textView6.setText(resultContainer.legs.get(0).train_name
                        + " &" + resultContainer.legs.get(1).train_name);
                holder.textView.setText("via " + toCamelCase(resultContainer.legs.get(0).station_name_end));
                holder.textView5.setText("Layover of " + (resultContainer.layover / 3600
                        + ":" + (((resultContainer.layover / 60 % 60) < 10) ? "0" : "")
                        + resultContainer.layover / 60 % 60) + " HRS");
            }

            holder.textView4.setText(resultContainer.total_duration / 3600
                    + ":" + (((resultContainer.total_duration / 60 % 60) < 10) ? "0" : "")
                    + resultContainer.total_duration / 60 % 60);

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

            // Log.v(MainActivity.TAG,"result time"+ resultElements.get(position).legs.get(0).arrival_start/3600+":"+(((resultElements.get(position).legs.get(0).arrival_start/60%60)<10)?"0":"")+ resultElements.get(position).legs.get(0).arrival_start/60%60);
            Date dateObj = sdf.parse(resultContainer.legs.get(0).arrival_start / 3600
                    + ":" + (((resultContainer.legs.get(0).arrival_start / 60 % 60) < 10) ? "0" : "")
                    + resultContainer.legs.get(0).arrival_start / 60 % 60);
            holder.textView2.setText(new SimpleDateFormat("h:mm a").format(dateObj));

            dateObj = sdf.parse(resultContainer.legs.get(resultContainer.legs.size() - 1).arrival_end / 3600
                    + ":" + (((resultContainer.legs.get(resultContainer.legs.size() - 1).arrival_end / 60 % 60) < 10) ? "0" : "")
                    + resultContainer.legs.get(resultContainer.legs.size() - 1).arrival_end / 60 % 60);
            holder.textView3.setText(new SimpleDateFormat("h:mm a").format(dateObj));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return resultElements.size();
    }

    private String toCamelCase(String s) {
        String str="";
        int a=0;
        while(a<s.length()){
            if(s.charAt(a)==' ') {
                str=str.concat(String.valueOf(" "+s.charAt(a + 1)).toUpperCase());
                a++;
            }else if(a==0)
                str=str.concat(String.valueOf(s.charAt(a)).toUpperCase());
            else
                str=str.concat(String.valueOf(s.charAt(a)).toLowerCase());
            a++;
        }
        return str;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textView, textView2, textView3, textView4, textView5, textView6;
        final LinearLayout linearLayout;
        int id=0;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedOption.selectedOptionID = id;
                    SelectedOption.element = new ResultContainer(JsonParser.resultContainerArrayList.get(id));
                    //Log.v(MainActivity.TAG, "bub" + JsonParser.resultContainerArrayList.get(id).legs.size() + "");
                    FragmentTransaction fragmentTransaction = ((Activity) v.getContext()).getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new SelectedOption(), "select").addToBackStack("sc3");
                    fragmentTransaction.commit();
                }
            });
            textView = itemView.findViewById(R.id.textView7);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView8);
            textView6 = itemView.findViewById(R.id.textView9);
            /*textView6 = (TextView) itemView.findViewById(R.id.textView3);
            textView7 = (TextView) itemView.findViewById(R.id.textView5);
            textView8 = (TextView) itemView.findViewById(R.id.textView22);
            textView9 = (TextView) itemView.findViewById(R.id.textView25);
            textView10 = (TextView) itemView.findViewById(R.id.textView32);
            textView11 = (TextView) itemView.findViewById(R.id.textView7);
            textView12 = (TextView) itemView.findViewById(R.id.textView2);
            textView13 = (TextView) itemView.findViewById(R.id.textView8);*/
            linearLayout = itemView.findViewById(R.id.linearLayout);
            /*imageView= (ImageView) itemView.findViewById(R.id.imageView2);*/
        }
    }
}