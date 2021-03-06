package com.example.eduardoi.locaplus.TelasListas;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eduardoi.locaplus.Dados.Banco;
import com.example.eduardoi.locaplus.Entidades.ClientesEntidade;
import com.example.eduardoi.locaplus.Entidades.VeiculoEntidade;
import com.example.eduardoi.locaplus.R;
import com.example.eduardoi.locaplus.TelasCadastros.CadastroCliente;
import com.example.eduardoi.locaplus.TelasCadastros.EditarRemoverCliente;
import com.example.eduardoi.locaplus.TelasCadastros.EditarRemoverLocacao;
import com.example.eduardoi.locaplus.TelasCadastros.EditarRemoverVeiculo;

import java.util.LinkedList;
import java.util.List;

public class ListarClientes extends AppCompatActivity {

    Banco bd;
    private ListView listaClientes;
    private SQLiteDatabase conexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listarclientes);
        listaClientes = findViewById(R.id.listarClientes);
        conexaoBD();
        acoes();
    }

    private void acoes() {
        listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent it = new Intent(ListarClientes.this, EditarRemoverCliente.class);
                ClientesEntidade novoCliente = (ClientesEntidade) adapterView.getItemAtPosition(position);
                it.putExtra("id", novoCliente.getId());
                it.putExtra("nome", novoCliente.getNomeCliente());
                it.putExtra("rg", novoCliente.getRgCliente());
                it.putExtra("cpf", novoCliente.getCpfCliente());
                it.putExtra("endereco", novoCliente.getEnderecoCliente());
                it.putExtra("cnh", novoCliente.getCnhCliente());
                it.putExtra("ndependentes", novoCliente.getNumeroDependentes());
                startActivity(it);
            }
        });
    }

    private void conexaoBD() {
        try {
            bd = new Banco(this);
            Toast.makeText(this, "Conexão Ok!", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setTitle("Erro");
            msg.setMessage("Erro ao conectar ao Banco");
            msg.setNeutralButton("Ok",null);
            msg.show();
        }
    }

    private List listaCliente(){
        conexao = bd.getWritableDatabase();
        List clientes =  new LinkedList();
        Cursor res = conexao.rawQuery("SELECT * FROM CLIENTE", null);
        if(res.getCount()>0){
            res.moveToFirst();
            do{
                ClientesEntidade cliente = new ClientesEntidade();
                cliente.setId(res.getInt(res.getColumnIndexOrThrow("ID")));
                cliente.setNomeCliente(res.getString(res.getColumnIndexOrThrow("NOME")));
                cliente.setRgCliente(res.getString(res.getColumnIndexOrThrow("RG")));
                cliente.setCpfCliente(res.getString(res.getColumnIndexOrThrow("CPF")));
                cliente.setEnderecoCliente(res.getString(res.getColumnIndexOrThrow("ENDERECO")));
                cliente.setCnhCliente(res.getString(res.getColumnIndexOrThrow("CNH")));
                cliente.setNumeroDependentes(res.getString(res.getColumnIndexOrThrow("NUMERODEPENDENTES")));
                clientes.add(cliente);
            }while (res.moveToNext());
        }
        return clientes;
    }

    public void acaoCadastrar(View view){
        Intent it = new Intent(ListarClientes.this, CadastroCliente.class);
        startActivity(it);
    }

    public void acaoSair(View view){
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        ArrayAdapter<ClientesEntidade> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listaCliente());
        listaClientes.setAdapter(arrayAdapter);
    }
}
