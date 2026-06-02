package client;

import client.builders.UserBuild;
import client.cli.*;
//import client.commands.*;
import client.builders.CoordinatesBuilder;
import client.builders.OrganizationBuilder;
import client.builders.WorkerMainBuilder;
import client.controllers.RunnerLoginController;
import client.controllers.RunnerMainController;
import client.network.ArgumentNetworkParse;
import client.network.NetworkClient;
import client.repository.*;
import common.repository.WorkerRepository;
import javafx.stage.Stage;


public class SystemBootstrapper {
    private InputProvider inputProvider;
    private Console console;
    private CommandRegistry commandRegistry;
    private ProxyWorkerRepository proxyWorkerRepository;
    private ScriptExecutionStack scriptExecutionStack;
    private final String[] argumentsFromMain;
    private WorkerMainBuilder workerBuilder;
    private NetworkClient networkClient;
    private UserRegistry userRegistry;
    private ClientSession clientSession;
    private RunnerLoginController runnerLoginController;
    private RunnerMainController runnerMainController;

    private final Stage primaryStage;


    public SystemBootstrapper(String[] argumentsFromMain, Stage primaryStage){
        this.argumentsFromMain = argumentsFromMain;
        this.primaryStage = primaryStage;
    }


    public Runner buildRunner() {
        initInfrastructure();
        initNetworkClient();
        initRepositories();
        initBuildersMainObject();
        initProxyRepository();
        initCommands();
        initRunnerControllers();
        return new Runner(runnerLoginController,runnerMainController);
    }









    private void initInfrastructure() {
        this.inputProvider = new InputProvider();
        this.console = new StandardConsole();

    }


    private void initNetworkClient(){
        var networkArguments = ArgumentNetworkParse.parseNetworkArguments(argumentsFromMain, console);
        this.networkClient = new NetworkClient(networkArguments.host(), networkArguments.port());
    }


    private void initProxyRepository(){
        this.proxyWorkerRepository = new ProxyWorkerRepository(networkClient, clientSession);
    }


    private void initRepositories() {
        this.userRegistry = new UserRegistry(networkClient);
        this.commandRegistry = new CommandRegistry(console);
        this.scriptExecutionStack = new ScriptExecutionStack(inputProvider, console);
        this.clientSession = new ClientSession();
    }


    private void initBuildersMainObject() {
        var coordinatesBuild = new CoordinatesBuilder(inputProvider, console);
        var organizationBuild = new OrganizationBuilder(inputProvider, console);
        this.workerBuilder = new WorkerMainBuilder(inputProvider, console);
        workerBuilder.setCoordinatesBuild(coordinatesBuild);
        workerBuilder.setOrganizationBuilder(organizationBuild);
    }


    private void initCommands() {
//        commandRegistry.addCommand(new ExitCommand());
//        commandRegistry.addCommand(new AddCommand(proxyWorkerRepository, console, workerBuilder));
//        commandRegistry.addCommand(new ShowCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new HelpCommand(commandRegistry, console));
//        commandRegistry.addCommand(new HistoryCommand(commandRegistry, console));
//        commandRegistry.addCommand(new ClearCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new UpdateByIdCommand(proxyWorkerRepository, console, workerBuilder));
//        commandRegistry.addCommand(new RemoveByIdCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new ExecuteScriptCommand(console, scriptExecutionStack));
//        commandRegistry.addCommand(new HeadCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new InfoCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new PrintFieldDescendingSalaryCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new RemoveAllByPosition(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new RemoveHeadCommand(proxyWorkerRepository, console));
//        commandRegistry.addCommand(new SumOfSalaryCommand(proxyWorkerRepository, console));

    }


    public UserRegistry getUserRegistry() {
        return userRegistry;
    }


    public ClientSession getClientSession() {
        return clientSession;
    }


    public WorkerRepository getProxyWorkerRepository() {
        return proxyWorkerRepository;
    }


    private void initRunnerControllers() {
        this.runnerLoginController = new RunnerLoginController(primaryStage);
        this.runnerMainController = new RunnerMainController(primaryStage);
        runnerMainController.setBootstrapper(this);
        runnerLoginController.setSystemBootstrapper(this);
        runnerLoginController.setRunnerMainController(runnerMainController);
    }
}