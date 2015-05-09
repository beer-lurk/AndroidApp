package pl.beerlurk.beerlurk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;
import pl.beerlurk.beerlurk.dto.DistancedBeerLocation;

public final class MyAdapter extends BaseAdapter {

    private List<DistancedBeerLocation> data;

    public MyAdapter(List<DistancedBeerLocation> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        TextView addressView = (TextView) convertView.findViewById(R.id.address);
        addressView.setText(data.get(position).getBeerLocation().getAddressWithName());
        Button button = (Button) convertView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ShowOnMapClickEvent(data.get(position)));
            }
        });
        return convertView;
    }
}
