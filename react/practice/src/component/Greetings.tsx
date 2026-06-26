type userCardProps={
    names:string;
};

function UserCard({names}:userCardProps){
    return (
        <>
        <h1>Hi {names} </h1>
        </>
    );
}

export default UserCard;