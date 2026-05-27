package constant;

public class FeedbackConstant {
    private FeedbackConstant() {}

    public static final String INVALID_FIELD = "Campo invalido";
    public static final String INVALID_NAME_FIELD = "El nombre no puede estar vacio o tener mas de 50 caracteres";
    public static final String INVALID_LASTNAME_FIELD = "El apellido no puede estar vacio o tener mas de 50 caracteres";
    public static final String INVALID_BALANCE_FIELD = "El monto a agregar no puede estar vacio";
    public static final String NOT_NUMERIC_BALANCE_FIELD = "El monto no puede contener letras o simbolos";
    public static final String NEGATIVE_BALANCE_FIELD = "El monto a agregar no puede ser un numero negativo";
    public static final String MAX_BALANCE_TO_ADD_FIELD = "El monto a agregar no puede ser superior a " + CommonConstant.MAX_BALANCE_TO_ADD;
    public static final String MAX_BALANCE_REACHED_FIELD = "La cuenta alcanzo el maximo de dinero (" + CommonConstant.MAX_BALANCE + ")";
    public static final String EMPTY_DESTINATION_ACCOUNT_FIELD = "La cuenta destino no puede estar vacia";
    public static final String DESTINATION_ACCOUNT_NOT_FOUND = "No se encontro la cuenta destino";
    public static final String CANNOT_TRANSFER_TO_SAME_PRODUCT = "No es posible transferirse a la misma cuenta";
    public static final String CURRENCY_ARE_NOT_SAME = "La moneda de las cuentas no son iguales";
    public static final String ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE = "La cuenta de origen no cuenta con fondos o excedio el limite de credito";
    public static final String PRODUCT_CARD_NOT_FOUND = "No se encontro el producto relacionado a la tarjeta";
    public static final String MAX_BALANCE_TO_TRANSFER_FIELD = "El monto a operar no puede ser superior a " + CommonConstant.MAX_BALANCE_TO_ADD;
    public static final String INVALID_DATE_FORMAT = "Ingresa un formato de fecha válido (" + CommonConstant.DAY_FORMAT + ").";
    public static final String TO_GREATER_THAN_FROM_DATE = "La fecha 'Desde' no puede ser posterior a la fecha 'Hasta'.";
    public static final String ERROR_TITLE = "Ups, Hubo un error!";
    public static final String ERROR_ADDING_CLIENT_MESSAGE = "Hubo un error al agregar al cliente a la base";
    public static final String ERROR_UPDATING_CLIENT_MESSAGE = "Hubo un error al actualizar al cliente a la base";
    public static final String ERROR_DELETING_CLIENT_MESSAGE = "Hubo un error al borrar al cliente de la tabla";
    public static final String ERROR_UPDATING_CLIENT_PRODUCTS_MESSAGE = "Hubo un error al modificar los productos del cliente";
    public static final String ERROR_ADDING_PRODUCT_BALANCE_MESSAGE = "Hubo un error al agregar dinero dentro de la cuenta";
    public static final String ERROR_TRANSFERING_MESSAGE = "Hubo un error al realizar la transferencia";
    public static final String ERROR_PAYING_MESSAGE = "Hubo un error al realizar el pago";

    public static final String INFO_DELETE_CLIENT = "Desea eliminar este cliente?";


    public static final String SUCCESS_TITLE = "Exito";
    public static final String TRANSFER_DONE = "Transferencia realizada";
    public static final String PAYMENT_DONE = "Pago realizado";
}
