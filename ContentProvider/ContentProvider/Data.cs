using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ContentProvider
{
    static class Data
    {
        static List<string> imgURL;

        static Data()
        {
            imgURL = new List<string>();
        }

        public static void Record(string value)
        {
            imgURL.Add(value);
        }

        public static string Read(int num)
        {
            return imgURL[num];
        }

        public static int Count()
        {
            return imgURL.Count;
        }

        public static void Clear()
        {
            imgURL.Clear();
        }
    }
}
