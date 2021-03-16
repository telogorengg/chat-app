package com.example.publicchat.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.publicchat.R;
import com.example.publicchat.model.objects.Messages;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PublicChatAdapter extends FirebaseRecyclerAdapter<Messages, PublicChatAdapter.MessagesViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private String username;

    public PublicChatAdapter(@NonNull FirebaseRecyclerOptions options, String username) {
        super(options);

        this.username = username;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessagesViewHolder viewHolder, int i, @NonNull Messages messages) {
        if(!messages.getMessageUser().equals(username))
        {
            viewHolder.profilePhoto.setVisibility(View.VISIBLE);
            viewHolder.usernameChatTv.setVisibility(View.VISIBLE);
            viewHolder.bubblePeople.setVisibility(View.VISIBLE);
            viewHolder.bubbleRed.setVisibility(View.GONE);
            viewHolder.messageChatRedTv.setVisibility(View.GONE);

            viewHolder.usernameChatTv.setText(messages.getMessageUser());
            viewHolder.messageChatPeopleTv.setText(messages.getMessageText());
        } else
        {
            viewHolder.profilePhoto.setVisibility(View.GONE);
            viewHolder.usernameChatTv.setVisibility(View.GONE);
            viewHolder.bubblePeople.setVisibility(View.GONE);
            viewHolder.bubbleRed.setVisibility(View.VISIBLE);
            viewHolder.messageChatRedTv.setVisibility(View.VISIBLE);

            viewHolder.messageChatRedTv.setText(messages.getMessageText());
        }
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout bubblePeople;
        private FrameLayout bubbleRed;
        private TextView usernameChatTv;
        private TextView messageChatPeopleTv;
        private TextView messageChatRedTv;
        private ImageView profilePhoto;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            bubblePeople = itemView.findViewById(R.id.bubble_people);
            bubbleRed = itemView.findViewById(R.id.bubble_red);
            usernameChatTv = itemView.findViewById(R.id.chat_username);
            messageChatPeopleTv = itemView.findViewById(R.id.chat_message);
            messageChatRedTv = itemView.findViewById(R.id.chat_message_red);
            profilePhoto = itemView.findViewById(R.id.chat_profile_photo);

            bubblePeople.setVisibility(View.GONE);
            bubbleRed.setVisibility(View.GONE);
        }
    }
}
