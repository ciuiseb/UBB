#include <iostream>
#include <mpi.h> 

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int procceses_count;
    MPI_Comm_size(MPI_COMM_WORLD, &procceses_count);
    int current_id;
    MPI_Comm_rank(MPI_COMM_WORLD, &current_id);

    const int n = 16;

    int* a = (int*)malloc(sizeof(int) * n);
    int* b = (int*)malloc(sizeof(int) * n);
    int* c = (int*)malloc(sizeof(int) * n);

    if (current_id == 0) {
        for (int i = 0; i < n; ++i) {
            a[i] = i;
            b[i] = i;
        }

        int each = n / (procceses_count - 1);
        int reamining = n % (procceses_count - 1);

        int start = 0;
        int end = each;

        for (int processI = 1; processI < procceses_count; ++processI) {
            if (reamining > 0) {
                ++end;
                --reamining;
            }
            MPI_Send(&start, 1, MPI_INT, processI, 0, MPI_COMM_WORLD);
            MPI_Send(&end, 1, MPI_INT, processI, 0, MPI_COMM_WORLD);
            MPI_Send(a + start, end - start, MPI_INT, processI, 0, MPI_COMM_WORLD);
            MPI_Send(b + start, end - start, MPI_INT, processI, 0, MPI_COMM_WORLD);

            start = end;
            end = start + each;

        }

        for (int i = 1; i < procceses_count; ++i) {
            MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(c + start, end - start,MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }
        for (int i = 0; i < n; ++i) {
            printf("a[%d] = %d ", i, a[i]);
        }
        printf("\n");
        for (int i = 0; i < n; ++i) {
            printf("b[%d] = %d ", i, b[i]);
        }
        printf("\n");

        for (int i = 0; i < n; ++i) {
            printf("c[%d] = %d ", i, c[i]);
        }
        printf("\n");

    }
    else {
        int start, end;
        MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(a + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(b + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        printf("Rank: %d start: %d end: %d \n", current_id, start, end);

        for (int i = start; i < end; ++i) {
            c[i] = a[i] + b[i];
        }
        MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(c + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);

    }

    MPI_Finalize();

    return 0;
}

const int N = 10;

void print(int* a, int n) {
    for (int i = 0; i < n; ++i) {
        std::cout << a[i] << ' ';
    }
    std::cout << std::endl;
}

int main(int argc, char** argv) {
    int namelen, myid, numprocs;
    MPI_Init(&argc, &argv); // aici initializam mpi -> se creaza un nou communicator, rank process si size
    MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    int a[N];
    int b[N];
    int c[N];

    MPI_Status status;
    //master are rank 0 by default
    if (myid == 0)
    {
        for (int i = 0; i < N; i++)
        {
            a[i] = rand() % 10;
            b[i] = rand() % 10;
        }

    }

    int send_count = N / numprocs;
    int* auxA = new int[send_count];
    int* auxB = new int[send_count];
    int* auxC = new int[send_count];

    MPI_Scatter(
        a, 
        send_count, 
        MPI_INT, 
        auxA, 
        send_count, 
        MPI_INT, 
        0, // masterul 
        MPI_COMM_WORLD
    );
    MPI_Scatter(
        b,
        send_count,
        MPI_INT,
        auxB,
        send_count,
        MPI_INT,
        0, // masterul 
        MPI_COMM_WORLD
    );

    for (int i = 0; i < send_count; ++i) {
        auxC[i] = auxA[i] + auxB[i];
    }

    MPI_Gather(
        auxC,
        send_count,
        MPI_INT,
        c,
        send_count,
        MPI_INT,
        0,
        MPI_COMM_WORLD
    );

    if (myid == 0) {
        print(a, N);
        print(b, N);
        print(c, N);
    }
    
    MPI_Finalize();
}

/*
    MPI_Scatter(
    void* send_data,
    int send_count,
    MPI_Datatype send_datatype,
    void* recv_data,
    int recv_count,
    MPI_Datatype recv_datatype,
    int root,
    MPI_Comm communicator)

    MPI_Gather(
    void* send_data,
    int send_count,
    MPI_Datatype send_datatype,
    void* recv_data,
    int recv_count,
    MPI_Datatype recv_datatype,
    int root,
    MPI_Comm communicator)
    */