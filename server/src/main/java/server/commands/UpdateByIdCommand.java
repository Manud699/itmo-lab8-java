package server.commands;

import common.model.Worker;
import common.network.Request;
import common.network.Response;
import common.network.Result;
import common.repository.WorkerRepository;

import java.util.Optional;

public class UpdateByIdCommand extends AbstractCommand {

    private final WorkerRepository workerRepository;

    public UpdateByIdCommand(WorkerRepository workerRepository) {
        super("update_by_id", "Updates a worker by ID");
        this.workerRepository = workerRepository;
    }

    @Override
    public Response execute(Request request) {
        Response error = validateNoArgument(request);
        if (error != null) {
            return error;
        }

        Optional<Worker> optionalWorker = request.getWorker();
        if (optionalWorker.isEmpty()) {
            return new Response(Result.failure("The updated worker data was not received"));
        }

        Result<Void> updateResult = workerRepository.updateWorkerById(optionalWorker.get());
        return new Response(updateResult);
    }
}