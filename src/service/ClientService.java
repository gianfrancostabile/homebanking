package service;

import exception.JDBCException;
import model.Client;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final ClientRepository repository = ClientRepository.getInstance();

    private static ClientService INSTANCE;
    private ClientService() {
    }

    public static ClientService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientService();
        }
        return INSTANCE;
    }

    public void insert(Client client) throws JDBCException {
        String id = repository.insert(client);
        client.setId(id);
    }

    public void update(Client client) throws JDBCException {
        repository.update(client.getId(), client);
    }

    public void deleteById(String id) throws JDBCException {
        repository.delete(id);
    }

    public void deleteAll() throws JDBCException {
        repository.deleteAll();
    }

    public List<Client> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
