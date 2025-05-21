using cs.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace cs.Repository.Interfaces;

public interface IRepository<ID, E> where E : IEntity<ID>
{
    E? FindOne(ID id);
    E? Save(E entity);
    E? Delete(ID id);
    E? Update(E entity);

    IEnumerable<E> FindAll();
}
