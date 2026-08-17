mod finance {
    pub struct Wallet {
        balance: i32, // private
    }

    impl Wallet {
        pub fn new() -> Self {
            Self { balance: 0 }
        }

        pub fn add_money(&mut self, amount: i32) {
            self.balance += amount;
        }

        pub fn check_balance(&self) -> i32 {
            self.balance
        }
    }
}

fn main() {
     let mut my_wallet = finance::Wallet::new();
     my_wallet.balance += 50; // error
     println!("Balance: {}", my_wallet.check_balance());
 }