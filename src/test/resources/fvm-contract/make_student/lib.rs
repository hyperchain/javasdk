use macros::contract;
use macros::storage;
use fvm_std::runtime;
use scale::{Decode, Encode};
use fvm_std::collections::hyper_map::HyperMap;

#[storage]
pub struct MyContract {

}

// #[derive(TypeInfo)]
#[derive(Encode, Decode)]
pub struct Student {
    age: u64,
}

#[contract]
impl MyContract {
    /// new方法是部署时的初始化方法，仅被调用一次
    fn new() -> Self{
        Self{

        }
    }

    pub fn make_student(&mut self, student: Student, _list: Student) -> Student {
        Student {
            age: student.age + _list.age
        }
    }
}