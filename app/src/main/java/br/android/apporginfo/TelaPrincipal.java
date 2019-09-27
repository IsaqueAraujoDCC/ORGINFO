package br.android.apporginfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DrawerListAdapter adapterLista;
    private ArrayList<Publicacao> mListaItens = new ArrayList<Publicacao>();
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference posts = firebaseDatabase.child("Posts");
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        recyclerView = (RecyclerView)findViewById(R.id.listaPost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterLista = new DrawerListAdapter(TelaPrincipal.this, mListaItens);
        recyclerView.setAdapter(adapterLista);

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListaItens.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.d("String", data.getKey());
                    List<String> comentarios;
                    comentarios = new ArrayList<>();
                    List<String> idsComentarios;
                    idsComentarios = new ArrayList<>();
                    for(DataSnapshot dataSnapshot2: data.child("comentarios").getChildren()){
                        idsComentarios.add(dataSnapshot2.getKey());
                        for(DataSnapshot dataSnapshot1: dataSnapshot2.getChildren()){
                            Log.d("valor", dataSnapshot1.getValue().toString());
                            comentarios.add(dataSnapshot1.getKey().toString()+": "+dataSnapshot1.getValue().toString());
                        }
                    }
                    int quantidadeLike = 0;
                    boolean curti = false;
                    for(DataSnapshot dataSnapshot1: data.child("quemcurtiu").getChildren()){
                        quantidadeLike++;
                        if(dataSnapshot1.getKey().toString().equals(firebaseAuth.getUid())){
                            curti = true;
                        }
                    }
                    //comentarios.add(data.child("comentarios").getValue().toString());
                        mListaItens.add(new Publicacao(data.getKey().toString(),data.child("endereco").getValue().toString(), data.child("titulo").getValue().toString(),
                                data.child("texto").getValue().toString(), quantidadeLike, comentarios, curti));
                    mListaItens.get(mListaItens.size()-1).setIdsComentarios(idsComentarios);

                }
                adapterLista.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private class LineHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titulo, texto, curtidas;
        EditText addComentario;
        ListView comentarios;
        Button addComentariobt, curtirbt;
        public LineHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imagem);
            titulo = (TextView)itemView.findViewById(R.id.titulo);
            texto = (TextView)itemView.findViewById(R.id.texto);
            curtidas = (TextView)itemView.findViewById(R.id.curtidas);
            addComentario = (EditText)itemView.findViewById(R.id.comentario);
            comentarios = (ListView) itemView.findViewById(R.id.listacomentario);
            addComentariobt = (Button)itemView.findViewById(R.id.addComentario);
            curtirbt = (Button)itemView.findViewById(R.id.curtir);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            //comentarios.setLayoutManager(layoutManager);
        }
    }
    private class DrawerListAdapter extends RecyclerView.Adapter<LineHolder> {
        Context mContext;
        ArrayList<Publicacao> mItens;

        public DrawerListAdapter(Context context, ArrayList<Publicacao> itens) {
            mContext = context;
            mItens = itens;
        }
        @Override
        public int getItemCount() {
            return mItens.size();
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LineHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post, parent, false));
        }
        @Override
        public void onBindViewHolder(final LineHolder holder, final int position) {
            holder.titulo.setText(mItens.get(position).getTitulo());
            holder.texto.setText(mItens.get(position).getTexto());
            holder.curtidas.setText(String.valueOf(mItens.get(position).getQuantCurtidas()));
            String[] array = new String[mItens.get(position).getComentarios().size()];
            mItens.get(position).getComentarios().toArray(array);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, array);
            //MyAdapter adapterLista2 = new MyAdapter(array);
            holder.comentarios.setAdapter(adapter);
            setListViewHeightBasedOnItems(holder.comentarios);
            adapter.notifyDataSetChanged();
            holder.comentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positionitem, long id) {
                    final String selected = mItens.get(position).getIdsComentarios().get(positionitem).toString();
                    /*if(posts.child(mItens.get(position).getId()).child("comentarios").child(selected).getKey() == "isaque"){
                        posts.child(mItens.get(position).getId()).child("comentarios").child(selected).removeValue();
                        adapter.notifyDataSetChanged();
                    }*/
                    //String selectedFromList = holder.comentarios.getAdapter().getItem(positionitem).toString();
                    //System.out.println("key =: "+ posts.child(mItens.get(position).getId()).child("comentarios").child(selected));
                    posts.child(mItens.get(position).getId()).child("comentarios").child(selected).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                if(dataSnapshot1.getKey().equals("lara")){
                                    System.out.println("key =: "+  dataSnapshot1.getKey());
                                    posts.child(mItens.get(position).getId()).child("comentarios").child(selected).removeValue();
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    System.out.println("Chosen Country = : " + selected);
                    /*if(mItens.get(position).getIdsComentarios().get(positionitem).){

                    }*/
                    /*String meuComentario = holder.addComentario.getText().toString();
                    mItens.get(position).setMeuComentario(meuComentario);
                    posts.child(mItens.get(position).getId()).child("comentarios").push().child("isaque").setValue(meuComentario);*/
                }
            });
            new DownloadImageTask(holder.imageView).execute(mItens.get(position).getEndereco());
            holder.addComentariobt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String meuComentario = holder.addComentario.getText().toString();
                    mItens.get(position).setMeuComentario(meuComentario);
                    posts.child(mItens.get(position).getId()).child("comentarios").push().child("isaque").setValue(meuComentario);
                }
            });
            if(mItens.get(position).isCurti() == true){
                holder.curtirbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posts.child(mItens.get(position).getId()).child("quemcurtiu").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                        mItens.get(position).setCurti(false);
                    }
                });
                holder.curtirbt.setText("Descurtir");
            }else{
                holder.curtirbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posts.child(mItens.get(position).getId()).child("quemcurtiu").child(firebaseAuth.getCurrentUser().getUid()).setValue("+1");
                        mItens.get(position).setCurti(true);
                    }
                });
                holder.curtirbt.setText("Curtir");
            }
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaListaPacotes.this, Detalhes.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("posicao", position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_open_scale, R.anim.slide_close_scale);
                }
            });*/
        }
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textView;
            public MyViewHolder(TextView v) {
                super(v);
                textView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textView.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
