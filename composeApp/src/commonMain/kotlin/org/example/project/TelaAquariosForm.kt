package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.viewmodel.AquarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAquariosForm(viewModel: AquarioViewModel, onVoltar: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (viewModel.idAtual == null) "Novo Aquário" else "Editar Aquário", fontWeight = FontWeight.Light, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(24.dp).fillMaxSize()) {
            OutlinedTextField(
                value = viewModel.formNome,
                onValueChange = { viewModel.formNome = it },
                label = { Text("Identificação") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = viewModel.formCapacidade,
                    onValueChange = { viewModel.formCapacidade = it },
                    label = { Text("Volume (L)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = viewModel.formInstalacao,
                    onValueChange = { viewModel.formInstalacao = it },
                    label = { Text("Data") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Botão Gravar
            Button(
                onClick = { viewModel.gravar(onSucesso = onVoltar) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Gravar", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Botão Limpar Exigido por Regulamento
            OutlinedButton(
                onClick = { viewModel.limparCampos() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Limpar Campos", fontSize = 16.sp)
            }
        }
    }
}