package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.Invitation;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class InviteRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Invitation> data;
    private final PublishSubject<UUID> onAccept = PublishSubject.create();
    private final PublishSubject<UUID> onDecline = PublishSubject.create();

    public static class InviteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView layout;
        public InviteViewHolder(CardView l) {
            super(l);
            layout = l;
        }
    }

    public InviteRecyclerAdapter() {
        this.data = FakeDatabase.getInstance().invitations;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public InviteRecyclerAdapter.InviteViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        CardView l = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invite_item_view, parent, false);
        InviteViewHolder vh = new InviteViewHolder(l);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Invitation elem = data.get(position);

        CardView layout = ((InviteRecyclerAdapter.InviteViewHolder)holder).layout;

        TextView title = layout.findViewById(R.id.invite_card_title);
        title.setText(elem.presentation.name);

        TextView content = layout.findViewById(R.id.invite_card_content);
        Button accept = layout.findViewById(R.id.invite_card_accept);
        Button decline = layout.findViewById(R.id.invite_card_decline);

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDecline.onNext(elem.id);
            }
        });

        switch (elem.type) {
            case NEW: {
                content.setText("Accept group collaboration request?");
                accept.setVisibility(View.VISIBLE);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAccept.onNext(elem.id);
                    }
                });
                break;
            }
            case DECLINED: {
                content.setText(FakeDatabase.getInstance().findUser(elem.receiverID).name + " declined your invitation");
                decline.setText("DISMISS");
                break;
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Observable<UUID> onAccept() {
        return onAccept.hide();
    }
    public Observable<UUID> onDecline() {
        return onDecline.hide();
    }
}
