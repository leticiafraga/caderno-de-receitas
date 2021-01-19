package com.leticiafraga.receitas.activity;

        import androidx.appcompat.app.AppCompatActivity;
        import android.app.AlertDialog;
        import android.content.ClipData;
        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.bumptech.glide.Glide;
        import com.google.firebase.storage.StorageReference;
        import com.leticiafraga.receitas.ConfigFirebase;
        import com.leticiafraga.receitas.R;
        import com.leticiafraga.receitas.Receita;

        import java.io.Serializable;

public class DetalhesActivity extends AppCompatActivity {

    private TextView campoDesc, campoFonte, campoIngr, campoNome, textFonte;
    private ImageView imageDetalhes;
    private String desc = "";
    private String fonte = "";
    private String ingr = "";
    private Receita receitaSelec;
    private String titulo = "";
    private StorageReference storageRef;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detalhes);
        campoNome = findViewById(R.id.textNomeDetalhe);
        campoDesc = findViewById(R.id.textDescDetalhe);
        campoIngr = findViewById(R.id.textIngrDetalhe);
        campoFonte = findViewById(R.id.textFonteDetalhe);
        textFonte = findViewById(R.id.textFonte);
        imageDetalhes = findViewById(R.id.imageDetalhes);
        receitaSelec = (Receita) getIntent().getSerializableExtra("receitaSelecionada");
        if (receitaSelec != null) {
            titulo = receitaSelec.getTitulo();
            desc = receitaSelec.getPreparo();
            ingr = receitaSelec.getIngredientes();
            fonte = receitaSelec.getFonte();
            String foto = receitaSelec.getFoto();
            if (foto != null){
                Glide.with(getApplicationContext()).load(foto).into(imageDetalhes);

            }else{
                imageDetalhes.setVisibility(View.INVISIBLE);
            }
        }
        campoNome.setText(titulo);
        campoDesc.setText(desc);
        campoIngr.setText(ingr);

        if (fonte == null || fonte.equals("")) {
            textFonte.setText("");
        } else {
            campoFonte.setText(fonte);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_editar:
                Intent intent = new Intent(getApplicationContext(), CadastrarReceitaActivity.class);
                intent.putExtra("receitaEditar", receitaSelec);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_excluir:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Certeza de que quer excluir?");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        receitaSelec.remover();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.menu_share:
                String strReceita = titulo + "\n\nIngredientes:\n" + ingr + "\n\nModo de preparo:\n" + desc;

                if (!(fonte == null || fonte.equals(""))) {
                    strReceita = strReceita + "\n\nFonte: " + fonte;
                }
                setClipboard(getApplicationContext(), strReceita);
                criarToast("Copiado!");
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // clipboard
    private void setClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("receita", text));
    }


    public void criarToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
