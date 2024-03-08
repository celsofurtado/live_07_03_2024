package br.com.fiap.live29_02

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.live29_02.model.Produto
import br.com.fiap.live29_02.repository.ProdutoRepository
import br.com.fiap.live29_02.ui.theme.Live2902Theme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Live2902Theme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          CadastroScreen()
        }
      }
    }
  }
}

@Composable
fun CadastroScreen() {

  val context = LocalContext.current
  val repository = ProdutoRepository(context)

  var nomeState by remember {
    mutableStateOf("")
  }

  var quantidadeState by remember {
    mutableStateOf("")
  }

  var dataState by remember {
    mutableStateOf("")
  }

  var disponivelState by remember {
    mutableStateOf(false)
  }

  var listaProdutosState = remember {
    mutableStateOf(listOf<Produto>())
  }

  listaProdutosState.value = repository.listarTodos()

  Column(
    modifier = Modifier.padding(16.dp)
  ) {
    Text(
      text = "Cadastro de produtos",
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(bottom = 8.dp)
    )
    OutlinedTextField(
      value = nomeState,
      onValueChange = {
        nomeState = it
      },
      label = { Text(text = "Nome do produto") },
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        keyboardType = KeyboardType.Email
      )
    )
    OutlinedTextField(
      value = quantidadeState,
      onValueChange = { quantidade ->
        quantidadeState = quantidade
      },
      label = { Text(text = "Quantidade em estoque") },
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        keyboardType = KeyboardType.Number
      )
    )
    OutlinedTextField(
      value = dataState,
      onValueChange = {
        dataState = it
      },
      label = { Text(text = "Data de vencimento") },
      modifier = Modifier.fillMaxWidth()
    )
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(
        checked = disponivelState,
        onCheckedChange = {
          disponivelState = it
        }
      )
      Text(text = "Disponível")
    }
    Button(
      onClick = {
        val produto = Produto(
          nome = nomeState,
          disponivel = disponivelState,
          quantidade = quantidadeState.toInt(),
          dataValidade = dataState
        )
        repository.salvar(produto)
        listaProdutosState.value = repository.listarTodos()
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "Salvar")
    }
    LazyColumn{
      items(listaProdutosState.value){ produto ->
        Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
          Column(modifier = Modifier.padding(8.dp)) {
            Text(text = produto.nome)
            Text(text = produto.dataValidade)
            Text(text = produto.quantidade.toString())
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CadastroScreenPreview() {
  Live2902Theme {
    CadastroScreen()
  }
}

