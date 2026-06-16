public void testBuscarDadosParceiro() {
    ClienteApiExterno cliente = new ClienteApiExterno();
    String resultado = cliente.buscarDadosParceiro("status");
    assertNotNull(resultado);
}